package com.cauc.mavenj.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.cauc.mavenj.R;
import com.cauc.mavenj.app.Constant;
import com.cauc.mavenj.callback.JsonCallback;
import com.cauc.mavenj.dialog.ForceUpdateDialog;
import com.cauc.mavenj.listener.PermissionListener;
import com.cauc.mavenj.model.CheckUpdateInfo;
import com.cauc.mavenj.model.LzyResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Headers;

/**
 * Created by Maven on 2017/4/10.
 * Email: cyjiang_11@163.com
 * Description:版本更新工具类
 */

public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private static Context mContext;
    private static String updatecontent;
    private static int opreate = 0;//0监测 1更新
    private static int versioncode;
    private static boolean isauto;

    private static ForceUpdateDialog mForceUpdateDialog;
    private static CheckUpdateInfo mCheckUpdateInfo;

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    public UpdateManager(Context context, boolean auto) {
        this.mContext = context;
        this.isauto = auto;
    }

    public void checkUpdate() {
        opreate = 0;
        final int code = ApplicationUtil.getVersionCode(mContext);
        OkGo.<LzyResponse<CheckUpdateInfo>>get(Constant.APP_VERSION_URL)
                .tag(mContext)
                .headers("header1", "headerValue1")//
                .params("param1", "paramValue1")//
                .execute(new JsonCallback<LzyResponse<CheckUpdateInfo>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CheckUpdateInfo>> response) {
                        handleResponse(response);
                        mCheckUpdateInfo = response.body().data;
                        Log.e(TAG, "onSuccess: versionCode------>" + mCheckUpdateInfo.getVersionCode());
                        Log.e(TAG, "onSuccess: versionCode------>" + code);
                        if (mCheckUpdateInfo.getVersionCode() > code) {
                            //老版本的更新dialog
                            updatecontent = mCheckUpdateInfo.getAppUpdateDesc();
                            showForceUpdateDialog(mCheckUpdateInfo);
                        } else {
//                            MToastUtil.showShort(mContext, R.string.is_newest_version);
                        }
                    }
                });

    }

    /**
     * 强制更新,checkupdatelibrary中提供的默认强制更新Dialog,您完全可以自定义自己的Dialog,
     */
    public synchronized ForceUpdateDialog showForceUpdateDialog(CheckUpdateInfo mCheckUpdateInfo) {
        mForceUpdateDialog = new ForceUpdateDialog(mContext);
        mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getAppSize())
                .setDownloadUrl(Constant.APP_DOWNLOAD_URL)
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getAppUpdateDesc())
                .setFileName("呱呱头条.apk")
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
        return mForceUpdateDialog;
    }

    protected static <T> void handleResponse(Response<T> response) {
        StringBuilder sb;
        Call call = response.getRawCall();
        if (call != null) {
            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ： ").append(requestHeadersString.get(name)).append("\n");
            }
        }
        T body = response.body();
        Log.e(TAG, "handleResponse: body----------->" + body);
        if (body == null) {

        } else {
            if (body instanceof String) {
            } else if (body instanceof List) {
                sb = new StringBuilder();
                List list = (List) body;
                for (Object obj : list) {
                    sb.append(obj.toString()).append("\n");
                }
            } else if (body instanceof Set) {
                sb = new StringBuilder();
                Set set = (Set) body;
                for (Object obj : set) {
                    sb.append(obj.toString()).append("\n");
                }
            } else if (body instanceof Map) {
                sb = new StringBuilder();
                Map map = (Map) body;
                Set keySet = map.keySet();
                for (Object key : keySet) {
                    sb.append(key.toString()).append(" ： ").append(map.get(key)).append("\n");
                }
            } else if (body instanceof File) {
                File file = (File) body;
            } else if (body instanceof Bitmap) {
            } else {
            }
        }

        okhttp3.Response rawResponse = response.getRawResponse();
        if (rawResponse != null) {
            Headers responseHeadersString = rawResponse.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("url ： ").append(rawResponse.request().url()).append("\n\n");
            sb.append("stateCode ： ").append(rawResponse.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
        }
    }

    public void download() {
        if (mForceUpdateDialog != null) {
            mForceUpdateDialog.download();
        }
    }
}
