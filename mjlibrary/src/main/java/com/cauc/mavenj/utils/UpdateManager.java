package com.cauc.mavenj.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nxt.zyl.R;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;
import com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog;

/**
 * Created by Maven on 2017/4/10.
 * Email: cyjiang_11@163.com
 * Description:版本更新工具类
 */

public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private Context mcontext;
    private boolean isnet;
    AlertDialog updatedialog;
    private String updatecontent;
    private int opreate = 0;//0监测 1更新
    private int versioncode;
    private boolean isauto;

    private ForceUpdateDialog mForceUpdateDialog;
    private CheckUpdateInfo mCheckUpdateInfo;


    public UpdateManager(Context context, boolean auto) {
        this.mcontext = context;
        this.isauto = auto;
    }

    public void checkUpdate() {
        isnet = CommonUtils.isNetWorkConnected(mcontext);
        if (isnet) {
            opreate = 0;

            final int code = new PackageUtils(mcontext).getVersionCode();

            OkGo.get(Constant.APP_VERSION_URL)
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e(TAG, "onSuccess: result----------->" + s);
                            mCheckUpdateInfo = new Gson().fromJson(s, new TypeToken<CheckUpdateInfo>() {
                            }.getType());
                            if (mCheckUpdateInfo.getVersionCode() > code) {
                                //老版本的更新dialog
//                                updatecontent = mCheckUpdateInfo.getAppUpdateDesc();
//                                getDialog().show();
                                showForceUpdateDialog(mCheckUpdateInfo);
                            } else {
                                if (!isauto)
                                    ZToastUtil.showShort(mcontext, R.string.is_newest_version);
                            }
                        }
                    });
        } else {
            ZToastUtil.showShort(mcontext, R.string.net_error);
        }
    }

    /**
     * 强制更新,checkupdatelibrary中提供的默认强制更新Dialog,您完全可以自定义自己的Dialog,
     */
    public void showForceUpdateDialog(CheckUpdateInfo mCheckUpdateInfo) {
        mForceUpdateDialog = new ForceUpdateDialog(mcontext);
        mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getAppSize())
                .setDownloadUrl(Constant.APP_DOWNLOAD_URL)
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getAppUpdateDesc())
                .setFileName("地情直报.apk")
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
    }

    //老版本更新方法，弃用
    public synchronized AlertDialog getDialog() {
        updatedialog = new AlertDialog.Builder(mcontext).create();
        View updateview = LayoutInflater.from(mcontext).inflate(R.layout.layout_update_dialog, null);
        updateview.findViewById(R.id.tv_title).setBackgroundColor(mcontext.getResources().getColor(R.color.colorPrimary));
        updatedialog.setView(updateview);
        TextView contentview = (TextView) updateview.findViewById(R.id.tv_dialog_content);

        if (!TextUtils.isEmpty(updatecontent)) contentview.setText(updatecontent);
        updateview.findViewById(R.id.tv_ignose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedialog.dismiss();
                // ZPreferenceUtils.setPrefBoolean(Constant.);
            }
        });
        updateview.findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedialog.dismiss();
                Uri uri = Uri.parse(Constant.APP_DOWNLOAD_URL);
                Intent web = new Intent(Intent.ACTION_VIEW, uri);
                mcontext.startActivity(web);
            }
        });
        return updatedialog;
    }
}
