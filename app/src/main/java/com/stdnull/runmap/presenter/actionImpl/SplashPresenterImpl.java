package com.stdnull.runmap.presenter.actionImpl;

import android.os.Handler;
import android.os.Looper;

import com.stdnull.runmap.presenter.action.ISplashPresenter;
import com.stdnull.runmap.ui.uibehavior.ISplashActivity;

/**
 * Created by chen on 2017/5/31.
 */

public class SplashPresenterImpl implements ISplashPresenter {
    private ISplashActivity mSplashActivity;

    public SplashPresenterImpl(ISplashActivity splashActivity) {
        this.mSplashActivity = splashActivity;
    }

    @Override
    public void startCountDown(int seconds) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashActivity.onCountDownFinished();
            }
        }, seconds * 1000);
    }
}
