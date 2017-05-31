package com.stdnull.runmap.presenter.action;

import android.content.Context;

import com.stdnull.runmap.ui.activity.BaseActivity;

/**
 * Created by chen on 2017/5/31.
 */

public interface IFunctionFragPresenter {
    void updateTotalDistance(Context context);
    void startTrackActivity(BaseActivity activity);
}
