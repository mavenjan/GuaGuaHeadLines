package com.nxt.maven.guaguaheadlines.guide.adapter;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxt.maven.guaguaheadlines.guide.fragment.BaseSceneFragment;
import com.nxt.maven.guaguaheadlines.guide.fragment.SceneOneFragment;
import com.nxt.maven.guaguaheadlines.guide.fragment.SceneTwoFragment;
import com.nxt.maven.guaguaheadlines.guide.fragment.SceneThreeFragment;

/**
 * Created by Jan Maven on 2017/7/17.
 * Email:cyjiang_11@163.com
 * Description:
 */

public class ScenePagerAdapter extends FragmentPagerAdapter {

    private SceneTransformer mSceneTransformer;

    public ScenePagerAdapter(@NonNull FragmentManager supportFragmentManager,
                             @NonNull SceneTransformer sceneTransformer) {
        super(supportFragmentManager);

        mSceneTransformer = sceneTransformer;
    }

    @Override
    public Fragment getItem(int position) {
        BaseSceneFragment baseSceneFragment = null;

        switch (position) {
            case 0:
                baseSceneFragment = SceneOneFragment.newInstance(position);
                break;
            case 1:
                baseSceneFragment = SceneTwoFragment.newInstance(position);
                break;
            case 2:
                baseSceneFragment = SceneThreeFragment.newInstance(position);
                break;
        }

        mSceneTransformer.addSceneChangeListener(baseSceneFragment);
        return baseSceneFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}