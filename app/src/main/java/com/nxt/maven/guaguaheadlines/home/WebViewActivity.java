package com.nxt.maven.guaguaheadlines.home;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nxt.maven.guaguaheadlines.R;
import com.nxt.maven.guaguaheadlines.base.MBaseActivity;
import com.nxt.maven.guaguaheadlines.base.BasePresenter;

import org.zackratos.ultimatebar.UltimateBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description: 加载网页的activity
 */

public class WebViewActivity extends MBaseActivity {
    public static final String URL = "url";

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_author)
    TextView mTvTitle;

    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;

    @BindView(R.id.wv_content)
    WebView mWvContent;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra(URL);
        mWvContent.loadUrl(url);
    }

    @Override
    public void initListener() {
        WebSettings settings = mWvContent.getSettings();
        settings.setJavaScriptEnabled(true);


        mWvContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mPbLoading.setVisibility(View.GONE);
            }
        });

        mWvContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mPbLoading.setProgress(newProgress);
            }
        });

        mWvContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWvContent.canGoBack()) {  //表示按返回键
                        mWvContent.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
