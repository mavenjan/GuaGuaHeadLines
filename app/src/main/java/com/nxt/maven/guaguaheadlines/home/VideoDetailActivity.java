package com.nxt.maven.guaguaheadlines.home;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.nxt.maven.guaguaheadlines.R;
import com.nxt.maven.guaguaheadlines.home.view.NewsDetailHeaderView;
import com.nxt.maven.guaguaheadlines.model.entity.NewsDetail;
import com.nxt.maven.guaguaheadlines.utils.UIUtils;
import com.nxt.maven.guaguaheadlines.utils.VideoPathDecoder;
import com.socks.library.KLog;

import org.zackratos.ultimatebar.UltimateBar;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description: 视频新闻
 */

public class VideoDetailActivity extends NewsDetailBaseActivity {

    @BindView(R.id.video_player)
    JCVideoPlayerStandard mVideoPlayer;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private SensorManager mSensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    private int mProgress;
    private int mPosition;
    private String mChannelCode;

    @Override
    public void initView() {
        super.initView();
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBarForDrawer(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    public void initData() {
        super.initData();
        mProgress = getIntent().getIntExtra(PROGRESS, 0);
    }

    @Override
    public void initListener() {
        super.initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        mVideoPlayer.setAllControlsVisible(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
        mVideoPlayer.titleTextView.setVisibility(GONE);
    }

    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        newsDetail.content = "";
        mHeaderView.setDetail(newsDetail, new NewsDetailHeaderView.LoadWebListener() {
            @Override
            public void onLoadFinished() {
                //加载完成后，显示内容布局
                mStateView.showContent();
            }
        });

        VideoPathDecoder decoder = new VideoPathDecoder() {
            @Override
            public void onSuccess(String url) {
                KLog.e("onGetNewsDetailSuccess", url);
                mVideoPlayer.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST, newsDetail.title);
                mVideoPlayer.seekToInAdvance = mProgress;//设置进度
                mVideoPlayer.startVideo();
            }

            @Override
            public void onDecodeError() {

            }
        };
        decoder.decodePath(newsDetail.url);
        KLog.e("onGetNewsDetailSuccess", newsDetail.url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        postVideoEvent(true);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        postVideoEvent(true);
    }
}
