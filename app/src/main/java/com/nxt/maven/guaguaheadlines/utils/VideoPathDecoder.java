package com.nxt.maven.guaguaheadlines.utils;

import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nxt.maven.guaguaheadlines.api.ApiRetrofit;
import com.nxt.maven.guaguaheadlines.home.model.response.VideoPathResponse;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description:
 */

public abstract class VideoPathDecoder {


    private static final String NICK = "chaychan";

    public void decodePath(String srcUrl) {
        WebView webView = new WebView(UIUtils.getContext());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//设置JS可用
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        ParseRelation relation = new ParseRelation(new IGetParamsListener() {
            @Override
            public void onGetParams(String r, String s) {
                sendRequest(srcUrl,r,s);
            }
        });

        webView.addJavascriptInterface(relation, NICK);//绑定JS和Java的联系类，以及使用到的昵称

        webView.loadUrl("file:///android_asset/parse.html");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:getParseParam('" + srcUrl + "')");
            }
        });
    }

    private void sendRequest(String srcUrl,String r,String s) {
        ApiRetrofit.getInstance().getApiService().getVideoPath(srcUrl,r,s) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideoPathResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getLocalizedMessage());
                        onDecodeError();
                    }

                    @Override
                    public void onNext(VideoPathResponse response) {
                        KLog.e("parseVideoResponse: " + response);
                        String url = "";
                        switch (response.retCode){
                            case 200:
                                //请求成功
                                List<VideoPathResponse.DataBean.VideoBean.DownloadBean> downloadList = response.data.video.download;
                                if (!ListUtils.isEmpty(downloadList)){
                                    url = downloadList.get(downloadList.size() -1).url;//获取下载地址中最后一个地址，即超清
                                    KLog.e("videoUrl: ",url);
                                }
                                onSuccess(url);
                                break;
                            case 400:
                                decodePath(srcUrl);//如果请求失败，可能是生成的r s请求参数不正确，重新再调用
                                break;
                        }
                    }
                });
    }


    public abstract void onSuccess(String url);

    public abstract void onDecodeError();


    private class ParseRelation {

        IGetParamsListener mListener;

        public ParseRelation(IGetParamsListener listener){
            mListener = listener;
        }

        @JavascriptInterface
        public void onReceiveParams(String r,String s) {
            mListener.onGetParams(r,s);
        }
    }

    public interface IGetParamsListener {
       void onGetParams(String r, String s);
    }

}
