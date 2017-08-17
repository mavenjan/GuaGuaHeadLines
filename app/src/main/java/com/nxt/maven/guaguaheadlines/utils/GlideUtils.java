package com.nxt.maven.guaguaheadlines.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nxt.maven.guaguaheadlines.R;

/**
 * Created by Jan Maven on 2017/8/14.
 * Email:cyjiang_11@163.com
 * Description: 对glide进行封装的工具类
 */

public class GlideUtils {

   public static void load(Context context,String url,ImageView iv){
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_default)
                .into(iv);
    }

    public static void load(Context context,String url,ImageView iv,int placeHolderResId){
        Glide.with(context)
                .load(url)
                .placeholder(placeHolderResId)
                .into(iv);
    }

    public static void loadRound(Context context,String url,ImageView iv){
        Glide.with(context)//
                .load(url)//
                .asBitmap()//
                .placeholder(R.mipmap.ic_circle_default)//
                .centerCrop()//
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
