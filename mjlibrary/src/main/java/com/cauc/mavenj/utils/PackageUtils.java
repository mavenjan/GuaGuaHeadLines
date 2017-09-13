package com.cauc.mavenj.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 *
 * Created by zhangyonglu on 2017/2/19.
 */

public class PackageUtils {
    private Context mcontext;

    public PackageUtils(Context context) {
        this.mcontext = context;

    }

    public String getVersionName() {
        try {
            PackageManager manager = mcontext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mcontext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getVersionCode() {
        try {
            PackageManager manager = mcontext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mcontext.getPackageName(), 0);

            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
