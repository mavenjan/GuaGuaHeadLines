package com.nxt.maven.guaguaheadlines.home.presenter;

import com.nxt.maven.guaguaheadlines.api.SubscriberCallBack;
import com.nxt.maven.guaguaheadlines.app.Constant;
import com.nxt.maven.guaguaheadlines.base.BasePresenter;
import com.nxt.maven.guaguaheadlines.home.model.entity.NewsDetail;
import com.nxt.maven.guaguaheadlines.home.model.response.CommentResponse;
import com.nxt.maven.guaguaheadlines.home.view.NewsDetailView;
import com.socks.library.KLog;

import rx.Subscriber;

/**
 * Created by Jan Maven on 2017/8/15.
 * Email:cyjiang_11@163.com
 * Description: 新闻详情获取数据的presenter
 */

public class NewsDetailPresenter extends BasePresenter<NewsDetailView> {

    public NewsDetailPresenter(NewsDetailView view) {
        super(view);
    }

    public void getComment(String groupId, String itemId, int pageNow) {
        int offset = (pageNow - 1) * Constant.COMMENT_PAGE_SIZE;
        addSubscription(mApiService.getComment(groupId, itemId, offset + "", String.valueOf(Constant.COMMENT_PAGE_SIZE)), new Subscriber<CommentResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
               e.printStackTrace();
                KLog.e(e.getLocalizedMessage());
                mView.onError();
            }

            @Override
            public void onNext(CommentResponse response) {
                mView.onGetCommentSuccess(response);
            }

        });
    }

    public void getNewsDetail(String url) {
        addSubscription(mApiService.getNewsDetail(url), new SubscriberCallBack<NewsDetail>() {

            @Override
            protected void onSuccess(NewsDetail response) {
                mView.onGetNewsDetailSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }
}
