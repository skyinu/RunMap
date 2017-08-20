package com.stdnull.runmap.ui.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stdnull.v2api.ui.V2MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment页面适配器类
 * Created by chen on 2017/1/28.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragments.add(FunctionFragment.newInstance());
        mFragments.add(PersonalFragment.newInstance());
        mFragments.add(V2MainFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
