package com.nxt.maven.guaguaheadlines.home.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Jan Maven on 2017/8/15.
 * Email:cyjiang_11@163.com
 * Description: TODO
 */
public class Channel implements MultiItemEntity {
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_MY_CHANNEL = 3;
    public static final int TYPE_OTHER_CHANNEL = 4;

    public String title;
    public String channelCode;
    public int itemType;

    public Channel(String title, String channelCode) {
        this(TYPE_MY_CHANNEL, title, channelCode);
    }

    public Channel(int type, String title, String channelCode) {
        this.title = title;
        this.channelCode = channelCode;
        this.itemType = type;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    @Override
    public int getItemType() {
        return this.itemType;
    }
}