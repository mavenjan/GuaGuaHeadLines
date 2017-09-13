package com.nxt.maven.guaguaheadlines;import android.content.Context;import android.content.Intent;import android.os.Build;import android.os.Handler;import android.os.Message;import android.view.WindowManager;import com.nxt.maven.guaguaheadlines.app.Constant;import com.nxt.maven.guaguaheadlines.base.BaseActivity;import com.nxt.maven.guaguaheadlines.guide.GuideActivity;import com.nxt.maven.guaguaheadlines.home.ui.MainActivity;import com.nxt.maven.guaguaheadlines.utils.SharePrefHelper;import org.zackratos.ultimatebar.UltimateBar;/** * Created by Jan Maven on 2017/7/17. * Email:cyjiang_11@163.com * Description: 启动页 */public class SplashActivity extends BaseActivity {    private boolean isFirstIn = true;    private static final int TIME = 2000;    private static final int GO_HOME = 1000;    private static final int GO_GUIDE = 1001;    private Handler mHandler = new Handler() {        @Override        public void handleMessage(Message msg) {            switch (msg.what) {                case GO_HOME:                    setGoHome();                    break;                case GO_GUIDE:                    setGoGuide();                    break;            }        }    };    @Override    protected Context getActivityContext() {        return this;    }    @Override    protected void initView() {        //沉浸模式        UltimateBar ultimateBar = new UltimateBar(this);        ultimateBar.setHintBar();        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();            localLayoutParams.flags =                    (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);        }        init();    }    @Override    protected int getLayout() {        return R.layout.activity_splash;    }    //判断是否加载引导界面    private void init() {        isFirstIn = SharePrefHelper.getBoolean(Constant.FIRST_IN, true);//        Log.e(TAG, "init: tag"+ isFirstIn );        if (isFirstIn) {            mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);        } else {            mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);        }    }    private void setGoHome() {        Intent i = new Intent(SplashActivity.this, MainActivity.class);        startActivity(i);        finish();    }    private void setGoGuide() {        Intent i = new Intent(SplashActivity.this, GuideActivity.class);        startActivity(i);        finish();    }}