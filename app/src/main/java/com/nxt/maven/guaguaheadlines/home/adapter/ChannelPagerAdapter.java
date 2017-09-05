package com.nxt.maven.guaguaheadlines.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.nxt.maven.guaguaheadlines.home.fragment.NewsListFragment;
import com.nxt.maven.guaguaheadlines.model.entity.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan Maven on 2017/8/15.
 * Email:cyjiang_11@163.com
 * Description: 频道的adapter
 */

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<NewsListFragment> mFragments;
    private List<Channel> mChannels;

    public ChannelPagerAdapter(List<NewsListFragment> fragmentList, List<Channel> channelList, FragmentManager fm) {
        super(fm);
        mFragments = fragmentList != null ? fragmentList : new ArrayList<>();
        mChannels = channelList != null ? channelList : new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).title;
    }
}
