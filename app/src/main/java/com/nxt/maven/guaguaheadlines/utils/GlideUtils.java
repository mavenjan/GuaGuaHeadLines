package com.nxt.maven.guaguaheadlines.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nxt.maven.guaguaheadlines.R;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description: 对glide进行封装的工具类
 */

public class GlideUtils {

    public static void load(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_default);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void load(Context context, String url, ImageView iv, int placeHolderResId) {
        if (placeHolderResId == -1) {
            Glide.with(context)
                    .load(url)
                    .into(iv);
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolderResId);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void loadRound(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_circle_default)
                .centerCrop()
                .circleCrop();

        Glide.with(context)//
                .load(url)//
                .apply(options)//
                .into(iv);
    }
}
