package com.stdnull.runmap.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.stdnull.runmap.R;
import com.stdnull.runmap.presenter.action.ISplashPresenter;
import com.stdnull.runmap.presenter.actionImpl.SplashPresenterImpl;
import com.stdnull.runmap.ui.uibehavior.ISplashActivity;

/**
 * 系统欢迎页
 * Created by chen on 2017/1/23.
 */

public class SplashActivity extends BaseActivity implements ISplashActivity {
    private ISplashPresenter mSplashPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.startCountDown(1);

    }

    @Override
    public void onCountDownFinished() {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
