package com.nxt.maven.guaguaheadlines.app;import android.app.Activity;import android.content.Context;import android.content.Intent;import android.os.Handler;import android.os.Looper;import android.support.multidex.MultiDex;import android.support.multidex.MultiDexApplication;import com.nxt.maven.guaguaheadlines.BuildConfig;import com.nxt.maven.guaguaheadlines.utils.SharePrefHelper;import com.socks.library.KLog;import org.litepal.LitePalApplication;import java.util.LinkedList;import java.util.List;/** * Created by Jan Maven on 2017/7/17. * Email:cyjiang_11@163.com * Description: */public class MyApplication  extends MultiDexApplication {    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费    private static Context mContext;//上下文    private static Thread mMainThread;//主线程    private static long mMainThreadId;//主线程id    private static Looper mMainLooper;//循环队列    private static Handler mHandler;//主线程Handler        private static MyApplication mAppContext;    private List<Activity> activityList = new LinkedList<Activity>();    public static Context applicationContext;    public final String PREF_USERNAME = "username";    @Override    protected void attachBaseContext(Context base) {        super.attachBaseContext(base);        MultiDex.install(this);    }        @Override    public void onCreate() {        super.onCreate();        //对全局属性赋值        mContext = getApplicationContext();        mMainThread = Thread.currentThread();        mMainThreadId = android.os.Process.myTid();        mHandler = new Handler();                //初始化首选项工具        mAppContext = this;        //**************************************相关第三方SDK的初始化等操作*************************************************        SharePrefHelper.getInstance(this);        KLog.init(BuildConfig.DEBUG);//初始化KLog        LitePalApplication.initialize(getApplicationContext());//初始化litePal    }    /**     * 重启当前应用     */    public static void restart() {        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        mContext.startActivity(intent);    }        public static synchronized MyApplication getAppContext() {        return mAppContext;    }    public void addActivity(Activity activity) {        activityList.add(activity);    }    public void exit() {        for (Activity activity : activityList) {            activity.finish();        }    }    public static Context getContext() {        return mContext;    }    public static void setContext(Context mContext) {        MyApplication.mContext = mContext;    }    public static Thread getMainThread() {        return mMainThread;    }    public static void setMainThread(Thread mMainThread) {        MyApplication.mMainThread = mMainThread;    }    public static long getMainThreadId() {        return mMainThreadId;    }    public static void setMainThreadId(long mMainThreadId) {        MyApplication.mMainThreadId = mMainThreadId;    }    public static Looper getMainThreadLooper() {        return mMainLooper;    }    public static void setMainThreadLooper(Looper mMainLooper) {        MyApplication.mMainLooper = mMainLooper;    }    public static Handler getMainHandler() {        return mHandler;    }    public static void setMainHandler(Handler mHandler) {        MyApplication.mHandler = mHandler;    }}