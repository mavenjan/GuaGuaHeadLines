package com.nxt.maven.guaguaheadlines.home.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nxt.maven.guaguaheadlines.R;
import com.nxt.maven.guaguaheadlines.model.entity.NewsDetail;
import com.nxt.maven.guaguaheadlines.utils.DateUtils;
import com.nxt.maven.guaguaheadlines.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jan Maven on 2017/7/17.
 * Email:cyjiang_11@163.com
 * Description: 新闻详情页头部
 */


public class NewsDetailHeaderView extends FrameLayout {


    @BindView(R.id.tvTitle)
    TextView mTvTitle;

    @BindView(R.id.ll_info)
    public LinearLayout mLlInfo;

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.tv_author)
    TextView mTvAuthor;

    @BindView(R.id.tv_time)
    TextView mTvTime;

    @BindView(R.id.wv_content)
    WebView mWvContent;

    private Context mContext;

    public NewsDetailHeaderView(Context context) {
        this(context, null);
    }

    public NewsDetailHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.header_news_detail, this);
        ButterKnife.bind(this, this);
    }

    public void setDetail(NewsDetail detail, LoadWebListener listener) {
        mWebListener = listener;

        mTvTitle.setText(detail.title);

        if (detail.media_user == null) {
            //如果没有用户信息
            mLlInfo.setVisibility(GONE);
        } else {
            if (!TextUtils.isEmpty(detail.media_user.avatar_url)){
                GlideUtils.loadRound(mContext, detail.media_user.avatar_url, mIvAvatar);
            }
            mTvAuthor.setText(detail.media_user.screen_name);
            mTvTime.setText(DateUtils.getShortTime(detail.publish_time * 1000L));
        }

        if (TextUtils.isEmpty(detail.content))
            mWvContent.setVisibility(GONE);


        String htmlPart1 = "<!DOCTYPE HTML html>\n" +
                "<head><meta charset=\"utf-8\"/>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=no\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<style> \n" +
                "img{max-width:100%!important;height:auto!important}\n" +
                " </style>";
        String htmlPart2 = "</body></html>";

        String html = htmlPart1 + detail.content + htmlPart2;


        mWvContent.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        mWvContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if (mWebListener != null){
                    mWebListener.onLoadFinished();
                }
            }
        });
    }

    private LoadWebListener mWebListener;

    /**页面加载的回调*/
    public interface LoadWebListener{
       void onLoadFinished();
    }
}
