package com.stdnull.runmap.presenter.actionImpl;

import android.content.Context;
import android.provider.Settings;

import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.presenter.action.IMainPresenter;
import com.stdnull.runmap.ui.uibehavior.IMainActivity;
import com.stdnull.runmap.utils.SystemUtils;

/**
 * Created by chen on 2017/5/31.
 */

public class MainPresenterImpl implements IMainPresenter {
    private IMainActivity mMainActivity;

    public MainPresenterImpl(IMainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    public void checkNetWork(Context context) {
        if(!SystemUtils.isNetworkEnable(GlobalApplication.getAppContext())){
            mMainActivity.showNetWorkHintDialog(Settings.ACTION_DATA_ROAMING_SETTINGS,context.getString(R.string.need_network));
        }
    }
}
