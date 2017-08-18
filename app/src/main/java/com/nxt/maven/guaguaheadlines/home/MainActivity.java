package com.nxt.maven.guaguaheadlines.home;import android.annotation.SuppressLint;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.content.Context;import android.content.Intent;import android.net.Uri;import android.os.Bundle;import android.support.design.widget.CoordinatorLayout;import android.support.design.widget.NavigationView;import android.support.design.widget.TabLayout;import android.support.v4.app.FragmentTransaction;import android.support.v4.content.ContextCompat;import android.support.v4.view.ViewPager;import android.support.v4.widget.DrawerLayout;import android.support.v7.app.AppCompatActivity;import android.support.v7.appcompat.BuildConfig;import android.text.TextUtils;import android.util.Log;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.Toast;import com.google.gson.Gson;import com.google.gson.reflect.TypeToken;import com.nxt.maven.guaguaheadlines.R;import com.nxt.maven.guaguaheadlines.app.Constant;import com.nxt.maven.guaguaheadlines.app.MBaseActivity;import com.nxt.maven.guaguaheadlines.app.MBaseActivity1;import com.nxt.maven.guaguaheadlines.app.MyApplication;import com.nxt.maven.guaguaheadlines.base.BasePresenter;import com.nxt.maven.guaguaheadlines.home.adapter.ChannelAdapter;import com.nxt.maven.guaguaheadlines.home.adapter.NewsAdapter;import com.nxt.maven.guaguaheadlines.home.adapter.TestFragmentAdapter;import com.nxt.maven.guaguaheadlines.home.behavior.TestFragment;import com.nxt.maven.guaguaheadlines.home.behavior.uc.UcNewsHeaderPagerBehavior;import com.nxt.maven.guaguaheadlines.model.entity.Channel;import com.nxt.maven.guaguaheadlines.utils.PreUtils;import com.nxt.maven.guaguaheadlines.utils.UIUtils;import com.socks.library.KLog;import org.zackratos.ultimatebar.UltimateBar;import java.nio.channels.Channels;import java.util.ArrayList;import java.util.List;import butterknife.BindView;import butterknife.OnClick;import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;import me.weyye.library.colortrackview.ColorTrackTabLayout;/** * Created by Jan Maven on 2017/7/17. * Email:cyjiang_11@163.com * Description: */public class MainActivity extends MBaseActivity1 implements        UcNewsHeaderPagerBehavior.OnPagerStateListener, TabLayout.OnTabSelectedListener {    private static final String TAG = "MainActivity";    @BindView(R.id.tab_channel)    ColorTrackTabLayout mTabChannel;    @BindView(R.id.iv_operation)    ImageView ivAddChannel;    @BindView(R.id.vp_content)    ViewPager mVpContent;    private List<Channel> mSelectedChannels = new ArrayList<>();    private List<Channel> mUnSelectedChannels = new ArrayList<>();//    private List<NewsListFragment> mChannelFragments = new ArrayList<>();    private List<NewsListFragment> mFragments = new ArrayList<>();    private UcNewsHeaderPagerBehavior mPagerBehavior;    private DrawerLayout mDrawerLayout;    private NavigationView mNavigationView;    private Gson mGson = new Gson();    @Override    public void initView() {        UltimateBar ultimateBar = new UltimateBar(this);        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.colorPrimary), 10);        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);        setupDrawerContent(mNavigationView);        findViewById(R.id.iv_github).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                openMyGitHub();            }        });        mPagerBehavior = (UcNewsHeaderPagerBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager).getLayoutParams()).getBehavior();        mPagerBehavior.setPagerStateListener(this);        initChannelData();        initChannelFragments();        initListener();//        mTabChannel.setTabMode(TabLayout.MODE_FIXED);//        mTabChannel.setOnTabSelectedListener(this);//        mVpContent.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabChannel));//        mVpContent.setAdapter(new TestFragmentAdapter(mFragments, getSupportFragmentManager()));    }    /**     * 初始化已选频道和未选频道的数据     */    private void initChannelData() {        String selectedChannelJson = PreUtils.getString(Constant.SELECTED_CHANNEL_JSON, "");        String unselectChannel = PreUtils.getString(Constant.UNSELECTED_CHANNEL_JSON, "");        if (TextUtils.isEmpty(selectedChannelJson) || TextUtils.isEmpty(unselectChannel)) {            //本地没有title            String[] channels = getResources().getStringArray(R.array.channel);            String[] channelCodes = getResources().getStringArray(R.array.channel_code);            //默认添加了全部频道            for (int i = 0; i < channelCodes.length; i++) {                String title = channels[i];                String code = channelCodes[i];                mSelectedChannels.add(new Channel(title, code));            }            selectedChannelJson = mGson.toJson(mSelectedChannels);//将集合转换成json字符串            KLog.i("selectedChannelJson:" + selectedChannelJson);            PreUtils.putString(Constant.SELECTED_CHANNEL_JSON, selectedChannelJson);//保存到sp        } else {            //之前添加过            List<Channel> selectedChannel = mGson.fromJson(selectedChannelJson, new TypeToken<List<Channel>>() {            }.getType());            List<Channel> unselectedChannel = mGson.fromJson(unselectChannel, new TypeToken<List<Channel>>() {            }.getType());            mSelectedChannels.addAll(selectedChannel);            mUnSelectedChannels.addAll(unselectedChannel);        }    }    /**     * 初始化已选频道的fragment的集合     */    private void initChannelFragments() {        KLog.e("initChannelFragments");        String[] channelCodes = getResources().getStringArray(R.array.channel_code);        for (Channel channel : mSelectedChannels) {            NewsListFragment newsFragment = new NewsListFragment();            Bundle bundle = new Bundle();            bundle.putString(Constant.CHANNEL_CODE, channel.channelCode);            bundle.putBoolean(Constant.IS_VIDEO_LIST, channel.channelCode.equals(channelCodes[1]));//是否是视频列表页面,根据判断频道号是否是视频            newsFragment.setArguments(bundle);            mFragments.add(newsFragment);//添加到集合中        }    }    public void initListener() {        ChannelAdapter channelAdapter = new ChannelAdapter(mFragments, mSelectedChannels,getSupportFragmentManager());        mVpContent.setAdapter(channelAdapter);        mVpContent.setOffscreenPageLimit(mSelectedChannels.size());        mTabChannel.setTabPaddingLeftAndRight(UIUtils.dip2Px(10), UIUtils.dip2Px(10));        mTabChannel.setupWithViewPager(mVpContent);        mTabChannel.post(new Runnable() {            @Override            public void run() {                //设置最小宽度，使其可以在滑动一部分距离                ViewGroup slidingTabStrip = (ViewGroup) mTabChannel.getChildAt(0);                slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + ivAddChannel.getMeasuredWidth());            }        });        //隐藏指示器        mTabChannel.setSelectedTabIndicatorHeight(0);        mVpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {            @Override            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {            }            @Override            public void onPageSelected(int position) {                //当页签切换的时候，如果有播放视频，则释放资源                JCVideoPlayer.releaseAllVideos();            }            @Override            public void onPageScrollStateChanged(int state) {            }        });    }    @OnClick({R.id.iv_github})    public void onClick(View view) {        switch (view.getId()) {            case R.id.iv_github:                break;        }    }    @Override    protected BasePresenter createPresenter() {        return null;    }    @Override    protected int provideContentViewId() {        return R.layout.activity_main;    }    @Override    public boolean enableSlideClose() {        return false;    }    private void openMyGitHub() {        Uri uri = Uri.parse("https://github.com/BCsl");        Intent it = new Intent(Intent.ACTION_VIEW, uri);        startActivity(it);    }    private void setupDrawerContent(NavigationView navigationView) {        navigationView.setNavigationItemSelectedListener(                new NavigationView.OnNavigationItemSelectedListener() {                    @Override                    public boolean onNavigationItemSelected(MenuItem menuItem) {//                        mMainPresenter.switchNavigation(menuItem.getItemId());//                        menuItem.setChecked(true);                        mDrawerLayout.closeDrawers();                        return true;                    }                });    }    @Override    public void onPagerClosed() {        if (BuildConfig.DEBUG) {            Log.d(TAG, "onPagerClosed: ");        }//        Snackbar.make(mNewsPager, "pager closed", Snackbar.LENGTH_SHORT).show();    }    @Override    public void onPagerOpened() {//        Snackbar.make(mNewsPager, "pager opened", Snackbar.LENGTH_SHORT).show();    }    @Override    protected void onStart() {        super.onStart();//        registerEventBus(MainActivity.this);    }    @Override    protected void onStop() {        super.onStop();//        unregisterEventBus(MainActivity.this);    }    @Override    protected void onPause() {        super.onPause();        JCVideoPlayer.releaseAllVideos();    }    @Override    public void onTabSelected(TabLayout.Tab tab) {        mVpContent.setCurrentItem(tab.getPosition());    }    @Override    public void onTabUnselected(TabLayout.Tab tab) {    }    @Override    public void onTabReselected(TabLayout.Tab tab) {    }    private long exitTime;    @Override    public void onBackPressed() {        if (JCVideoPlayer.backPress()) {            return;        }        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {            mDrawerLayout.closeDrawers();        } else {            if (mPagerBehavior != null && mPagerBehavior.isClosed()) {                mPagerBehavior.openPager();            } else {                if ((System.currentTimeMillis() - exitTime) > 2000) {                    Toast.makeText(getApplicationContext(), getString(R.string.exit_notice),                            Toast.LENGTH_SHORT).show();                    exitTime = System.currentTimeMillis();                } else {                    MyApplication.getAppContext().exit();                    finish();                }//            super.onBackPressed();            }        }    }}