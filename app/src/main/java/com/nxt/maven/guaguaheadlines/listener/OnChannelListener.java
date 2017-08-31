package com.nxt.maven.guaguaheadlines.listener;

/**
 * Created by Jan Maven on 2017/8/29.
 * Email:cyjiang_11@163.com
 * Description:
 */

public interface OnChannelListener {
    void onItemMove(int starPos, int endPos);
    void onMoveToMyChannel(int starPos, int endPos);
    void onMoveToOtherChannel(int starPos, int endPos);
}
