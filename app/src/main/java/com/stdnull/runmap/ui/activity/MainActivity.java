package com.stdnull.runmap.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.stdnull.runmap.R;
import com.stdnull.runmap.presenter.action.IMainPresenter;
import com.stdnull.runmap.presenter.actionImpl.MainPresenterImpl;
import com.stdnull.runmap.ui.frag.FragmentAdapter;
import com.stdnull.runmap.ui.uibehavior.IMainActivity;
import com.stdnull.runmap.ui.view.PagerIndicator;

/**
 * 系统主页面
 * Created by chen on 2017/1/28.
 */

public class MainActivity extends BaseActivity implements IMainActivity {
    private ViewPager mFragmentContainer;
    private PagerIndicator mPagerIndicator;
    private FragmentAdapter mFragmentAdapter;
    private static String[] mFragmentTitles = new String[]{"轨迹","我的"};
    private IMainPresenter mMainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    protected void init(){
        mMainPresenter = new MainPresenterImpl(this);
        mMainPresenter.checkNetWork(this);
        mFragmentContainer = (ViewPager) findViewById(R.id.fragment_container);
        mPagerIndicator = (PagerIndicator) findViewById(R.id.pager_indicator);
        mPagerIndicator.setTitle(mFragmentTitles);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragmentContainer.setAdapter(mFragmentAdapter);
    }

    @Override
    public void showNetWorkHintDialog(String action, String title) {
        showSettingDialog(action,title).show();
    }
}
