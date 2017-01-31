package com.stdnull.runmap.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;

import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.activity.frag.FragmentAdapter;
import com.stdnull.runmap.ui.PagerIndicator;
import com.stdnull.runmap.utils.SystemUtils;

/**
 * Created by chen on 2017/1/28.
 */

public class MainActivity extends BaseActivity {
    private ViewPager mFragmentContainer;
    private PagerIndicator mPagerIndicator;
    private FragmentAdapter mFragmentAdapter;
    private static String[] mFragmentTitles = new String[]{"轨迹","我的"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    protected void init(){
        initConfig();
        initView();
    }

    private void initConfig() {
        if(!SystemUtils.isNetworkEnable(GlobalApplication.getAppContext())){
            showSettingDialog(Settings.ACTION_NETWORK_OPERATOR_SETTINGS,getString(R.string.need_network));
        }
    }

    protected void initView(){
        mFragmentContainer = (ViewPager) findViewById(R.id.fragment_container);
        mPagerIndicator = (PagerIndicator) findViewById(R.id.pager_indicator);
        mPagerIndicator.setTitle(mFragmentTitles);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentContainer.setAdapter(mFragmentAdapter);
    }
}
