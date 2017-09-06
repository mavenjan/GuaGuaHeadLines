package com.nxt.maven.guaguaheadlines.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Jan Maven on 2017/8/3.
 * Email:cyjiang_11@163.com
 * Description:
 */

public class TestFragmentAdapter extends FragmentStatePagerAdapter {
    List<? extends Fragment> mFragments;


    public TestFragmentAdapter(List<? extends Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
