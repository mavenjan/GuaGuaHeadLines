package com.nxt.maven.guaguaheadlines.home.presenter;


import android.util.Log;

import com.google.gson.Gson;
import com.nxt.maven.guaguaheadlines.base.BasePresenter;
import com.nxt.maven.guaguaheadlines.home.model.entity.News;
import com.nxt.maven.guaguaheadlines.home.model.entity.NewsData;
import com.nxt.maven.guaguaheadlines.home.model.response.NewsResponse;
import com.nxt.maven.guaguaheadlines.utils.ListUtils;
import com.nxt.maven.guaguaheadlines.utils.PreUtils;
import com.socks.library.KLog;
import com.nxt.maven.guaguaheadlines.home.view.NewsListView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Jan Maven on 2017/8/15.
 * Email:cyjiang_11@163.com
 * Description: 新闻列表的presenter
 */

public class NewsListPresenter extends BasePresenter<NewsListView> {
    private static final String TAG = "NewsListPresenter";

    private long lastTime;

    public NewsListPresenter(NewsListView view) {
        super(view);
    }


    public void getNewsList(String channelCode) {
        lastTime = PreUtils.getLong(channelCode, 0);//读取对应频道下最后一次刷新的时间戳
        if (lastTime == 0) {
            //如果为空，则是从来没有刷新过，使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }
        Log.e(TAG, "getNewsList: channelCode------------->" + channelCode);
        Log.e(TAG, "getNewsList: lastTime------------->" + lastTime);
        Log.e(TAG, "getNewsList: currentTimeMillis------------->" + System.currentTimeMillis() / 1000);
        addSubscription(mApiService.getNewsList(channelCode, lastTime, System.currentTimeMillis() / 1000), new Subscriber<NewsResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e.getLocalizedMessage());
                mView.onError();
            }

            @Override
            public void onNext(NewsResponse response) {
                lastTime = System.currentTimeMillis() / 1000;
                PreUtils.putLong(channelCode, lastTime);//保存刷新的时间戳

                List<NewsData> data = response.data;
                List<News> newsList = new ArrayList<>();
                if (!ListUtils.isEmpty(data)) {
                    for (NewsData newsData : data) {
                        News news = new Gson().fromJson(newsData.content, News.class);
                        newsList.add(news);
                    }
                }
                KLog.e(newsList);
                mView.onGetNewsListSuccess(newsList, response.tips.display_info);
            }
        });
    }
}
