package com.nxt.maven.guaguaheadlines.home;import android.annotation.SuppressLint;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.content.Context;import android.content.Intent;import android.net.Uri;import android.os.Bundle;import android.support.design.widget.CoordinatorLayout;import android.support.design.widget.NavigationView;import android.support.design.widget.TabLayout;import android.support.v4.app.FragmentTransaction;import android.support.v4.content.ContextCompat;import android.support.v4.view.ViewPager;import android.support.v4.widget.DrawerLayout;import android.support.v7.app.AppCompatActivity;import android.support.v7.appcompat.BuildConfig;import android.util.Log;import android.view.MenuItem;import android.view.View;import android.widget.Toast;import com.nxt.maven.guaguaheadlines.R;import com.nxt.maven.guaguaheadlines.app.MBaseActivity;import com.nxt.maven.guaguaheadlines.app.MyApplication;import com.nxt.maven.guaguaheadlines.base.BasePresenter;import com.nxt.maven.guaguaheadlines.home.adapter.TestFragmentAdapter;import com.nxt.maven.guaguaheadlines.home.behavior.TestFragment;import com.nxt.maven.guaguaheadlines.home.behavior.uc.UcNewsHeaderPagerBehavior;import org.zackratos.ultimatebar.UltimateBar;import java.util.ArrayList;import java.util.List;import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;import me.weyye.library.colortrackview.ColorTrackTabLayout;/** * Created by Jan Maven on 2017/7/17. * Email:cyjiang_11@163.com * Description: */public class MainActivity extends MBaseActivity implements        UcNewsHeaderPagerBehavior.OnPagerStateListener {    private static final String TAG = "MainActivity";    private ViewPager mNewsPager;//    private ColorTrackTabLayout mTableLayout;    private List<TestFragment> mFragments;    private UcNewsHeaderPagerBehavior mPagerBehavior;    private DrawerLayout mDrawerLayout;    private NavigationView mNavigationView;    @Override    public void initView() {        UltimateBar ultimateBar = new UltimateBar(this);        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.colorPrimary), 10);        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);        setupDrawerContent(mNavigationView);        findViewById(R.id.iv_github).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                openMyGitHub();            }        });        mPagerBehavior = (UcNewsHeaderPagerBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager).getLayoutParams()).getBehavior();        mPagerBehavior.setPagerStateListener(this);        mNewsPager = (ViewPager) findViewById(R.id.id_uc_news_content);//        mTableLayout = (ColorTrackTabLayout) findViewById(R.id.id_uc_news_tab);//        mFragments = new ArrayList<TestFragment>();//        for (int i = 0; i < 4; i++) {//            mFragments.add(TestFragment.newInstance(String.valueOf(i), false));//            mTableLayout.addTab(mTableLayout.newTab().setText("Tab" + i));//        }//        mTableLayout.setTabMode(ColorTrackTabLayout.MODE_FIXED);//        mTableLayout.setOnTabSelectedListener(this);//        mNewsPager.addOnPageChangeListener(new ColorTrackTabLayout.TabLayoutOnPageChangeListener(mTableLayout));//        mNewsPager.setAdapter(new TestFragmentAdapter(mFragments, getSupportFragmentManager()));        //步骤一：添加一个FragmentTransaction的实例        FragmentManager fragmentManager = getSupportFragmentManager();        FragmentTransaction transaction = fragmentManager.beginTransaction();        //步骤二：用add()方法加上Fragment的对象rightFragment        HomeFragment rightFragment = new HomeFragment();        transaction.add(R.id.id_uc_news_content, rightFragment);        //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效        transaction.commit();        //如果当前页码和点击的页码一致,进行下拉刷新        String channelCode = "";//        if (position == 0) {//            channelCode = ((HomeFragment) mFragments.get(0)).getCurrentChannelCode();//获取到首页当前显示的fragment的频道//        } else {//            channelCode = ((VideoFragment) mFragments.get(1)).getCurrentChannelCode();//获取到视频当前显示的fragment的频道//        }//        postTabRefreshEvent(bottomBarItem, position, channelCode);//发送下拉刷新的事件    }    @Override    protected BasePresenter createPresenter() {        return null;    }    @Override    protected int provideContentViewId() {        return R.layout.activity_main;    }    @Override    public boolean enableSlideClose() {        return false;    }    private void openMyGitHub() {        Uri uri = Uri.parse("https://github.com/BCsl");        Intent it = new Intent(Intent.ACTION_VIEW, uri);        startActivity(it);    }    private void setupDrawerContent(NavigationView navigationView) {        navigationView.setNavigationItemSelectedListener(                new NavigationView.OnNavigationItemSelectedListener() {                    @Override                    public boolean onNavigationItemSelected(MenuItem menuItem) {//                        mMainPresenter.switchNavigation(menuItem.getItemId());//                        menuItem.setChecked(true);                        mDrawerLayout.closeDrawers();                        return true;                    }                });    }//    @Override//    public void onTabSelected(ColorTrackTabLayout.Tab tab) {//        mNewsPager.setCurrentItem(tab.getPosition());//    }////    @Override//    public void onTabUnselected(ColorTrackTabLayout.Tab tab) {////    }////    @Override//    public void onTabReselected(ColorTrackTabLayout.Tab tab) {////    }    @Override    public void onPagerClosed() {        if (BuildConfig.DEBUG) {            Log.d(TAG, "onPagerClosed: ");        }//        Snackbar.make(mNewsPager, "pager closed", Snackbar.LENGTH_SHORT).show();    }    @Override    public void onPagerOpened() {//        Snackbar.make(mNewsPager, "pager opened", Snackbar.LENGTH_SHORT).show();    }    private long exitTime;    @Override    protected void onStart() {        super.onStart();        registerEventBus(MainActivity.this);    }    @Override    protected void onStop() {        super.onStop();        unregisterEventBus(MainActivity.this);    }    @Override    protected void onPause() {        super.onPause();        JCVideoPlayer.releaseAllVideos();    }    @Override    public void onBackPressed() {        if (JCVideoPlayer.backPress()) {            return;        }        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {            mDrawerLayout.closeDrawers();        } else {            if (mPagerBehavior != null && mPagerBehavior.isClosed()) {                mPagerBehavior.openPager();            } else {                if ((System.currentTimeMillis() - exitTime) > 2000) {                    Toast.makeText(getApplicationContext(), getString(R.string.exit_notice),                            Toast.LENGTH_SHORT).show();                    exitTime = System.currentTimeMillis();                } else {                    MyApplication.getAppContext().exit();                    finish();                }//            super.onBackPressed();            }        }    }}