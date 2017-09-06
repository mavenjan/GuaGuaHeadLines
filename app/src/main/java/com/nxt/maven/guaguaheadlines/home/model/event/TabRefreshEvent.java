package com.nxt.maven.guaguaheadlines.home.model.event;


import com.chaychan.library.BottomBarItem;

/**
 * Created by Jan Maven on 2017/8/15.
 * Email:cyjiang_11@163.com
 * Description: 下拉刷新的事件
 */

public class TabRefreshEvent {
    /**
     * 频道
     */
    private String channelCode;
    private BottomBarItem bottomBarItem;
    private boolean isHomeTab;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public BottomBarItem getBottomBarItem() {
        return bottomBarItem;
    }

    public void setBottomBarItem(BottomBarItem bottomBarItem) {
        this.bottomBarItem = bottomBarItem;
    }

    public boolean isHomeTab() {
        return isHomeTab;
    }

    public void setHomeTab(boolean homeTab) {
        isHomeTab = homeTab;
    }
}
