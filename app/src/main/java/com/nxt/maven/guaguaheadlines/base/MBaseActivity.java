package com.nxt.maven.guaguaheadlines.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.chaychan.lib.SlidingLayout;
import com.github.nukc.stateview.StateView;
import com.nxt.maven.guaguaheadlines.home.ui.MainActivity;
import com.cauc.mavenj.listener.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import butterknife.ButterKnife;

/**
 * Created by Jan Maven on 2017/8/15.
 * Email:cyjiang_11@163.com
 * Description: activity的基类
 */

public abstract class MBaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T mPresenter;
    private static long mPreTime;
    private static Activity mCurrentActivity;// 对所有activity进行管理
    public static List<Activity> mActivities = new LinkedList<Activity>();
    protected Bundle savedInstanceState;
    protected StateView mStateView;

    public final String TAG = getClass().getSimpleName();


    public PermissionListener mPermissionListener;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (enableSlideClose()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }

        this.savedInstanceState = savedInstanceState;

        //初始化的时候将其添加到集合中
        synchronized (mActivities) {
            mActivities.add(this);
        }

        mPresenter = createPresenter();

        //子类不再需要设置布局ID，也不再需要使用ButterKnife.bind()
        setContentView(provideContentViewId());
        ButterKnife.bind(this);

        initView();
        initData();
        initListener();
    }


    public boolean enableSlideClose() {
        return true;
    }

    public void initView() {
    }

    public void initData() {
    }

    public void initListener() {
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //销毁的时候从集合中移除
        synchronized (mActivities) {
            mActivities.remove(this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 退出应用的方法
     */
    public static void exitApp() {

        ListIterator<Activity> iterator = mActivities.listIterator();

        while (iterator.hasNext()) {
            Activity next = iterator.next();
            next.finish();
        }
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * 统一退出控制
     */
    @Override
    public void onBackPressed() {
        if (mCurrentActivity instanceof MainActivity) {
            //如果是主页面
            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2秒
//                UIUtils.showToast("再按一次，退出应用");
                mPreTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();// finish()
    }

    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }

    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        Log.e(TAG, "onRequestPermissionsResult: permission-------------->" + permission);
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }

//    //单个权限的检查
//    public void checkPermission(@NonNull final String permission, @Nullable String reason) {
//        if (Build.VERSION.SDK_INT < 23) return;
//        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            //权限已经申请
//            onPermissionGranted(permission);
//
//        } else {
//            if (!TextUtils.isEmpty(reason)) {
//                //判断用户先前是否拒绝过该权限申请，如果为true，我们可以向用户解释为什么使用该权限
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                    //这里的dialog可以自定义
//                    new AlertDialog.Builder(this).setCancelable(false).setTitle("温馨提示").setMessage(reason).
//                            setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    requestPermission(new String[]{permission});
//                                    dialog.dismiss();
//                                }
//                            }).show();
//                } else {
//                    requestPermission(new String[]{permission});
//                }
//            } else {
//                requestPermission(new String[]{permission});
//            }
//
//        }
//    }

//    //多个权限的检查
//    public void checkPermissions(@NonNull String... permissions) {
//        if (Build.VERSION.SDK_INT < 23) return;
//        //用于记录权限申请被拒绝的权限集合
//        List<String> permissionDeniedList = new ArrayList<>();
//        for (String permission : permissions) {
//            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                onPermissionGranted(permission);
//            } else {
//                permissionDeniedList.add(permission);
//            }
//        }
//        if (!permissionDeniedList.isEmpty()) {
//            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
//            requestPermission(deniedPermissions);
//        }
//    }
//
//    //调用系统API完成权限申请
//    private void requestPermission(String[] permissions) {
//        ActivityCompat.requestPermissions(this, permissions, YZT_PERMISSION_REQUEST);
//    }
//
//    //申请权限被允许的回调
//    public void onPermissionGranted(String permission) {
//
//    }
//
//    //申请权限被拒绝的回调
//    public void onPermissionDenied(String permission) {
//
//    }
//
//    //申请权限的失败的回调
//    public void onPermissionFailure() {
//
//    }
//
//    //如果从设置界面返回，则重新申请权限
//    public void onRecheckPermission() {
//
//    }
//
//    //弹出系统权限询问对话框，用户交互后的结果回调
//    @Override
//    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case YZT_PERMISSION_REQUEST:
//                if (grantResults.length > 0) {
//                    //用于记录是否有权限申请被拒绝的标记
//                    boolean isDenied = false;
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            onPermissionGranted(permissions[i]);
//                        } else {
//                            isDenied = true;
//                            onPermissionDenied(permissions[i]);
//                        }
//                    }
//                    if (isDenied) {
//                        isDenied = false;
//                        //如果有权限申请被拒绝，则弹出对话框提示用户去修改权限设置。
//                        showPermissionSettingsDialog();
//                    }
//
//                } else {
//                    onPermissionFailure();
//                }
//                break;
//        }
//    }
//
//    private void showPermissionSettingsDialog() {
//        new AlertDialog.Builder(this).setCancelable(false).setTitle("温馨提示").
//                setMessage("缺少必要权限\n不然将导致部分功能无法正常使用").setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                jump2PermissionSettings();
//            }
//        }).show();
//    }
//
//    /**
//     * 跳转到应用程序信息详情页面
//     */
//    private void jump2PermissionSettings() {
//        mIsJump2Settings = true;
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivity(intent);
//    }
}
