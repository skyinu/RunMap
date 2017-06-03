package com.stdnull.runmap.presenter.actionImpl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.presenter.action.IFunctionFragPresenter;
import com.stdnull.runmap.ui.activity.BaseActivity;
import com.stdnull.runmap.ui.activity.MovementTrackActivity;
import com.stdnull.runmap.ui.uibehavior.IFunctionFragment;
import com.stdnull.runmap.utils.SystemUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by chen on 2017/5/31.
 */

public class FunctionFragPresenterImpl implements IFunctionFragPresenter {
    private IFunctionFragment mFunctionFragment;

    public FunctionFragPresenterImpl(IFunctionFragment functionFragment) {
        this.mFunctionFragment = functionFragment;
    }

    @Override
    public void updateTotalDistance(Context context) {
        if(context != null){
            SharedPreferences sp = context.getSharedPreferences(RMConfiguration.FILE_CONFIG, Context.MODE_PRIVATE);
            long distance = sp.getLong(RMConfiguration.KEY_TOTAL_DISTANCE,0);
            long tmpDistance = sp.getLong(RMConfiguration.KEY_TMP_DISTANCE,0);
            if(tmpDistance < 0){
                return;
            }
            distance += tmpDistance;
            //if there have an effective value, update it now
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(RMConfiguration.KEY_TMP_DISTANCE,0);
            editor.putLong(RMConfiguration.KEY_TOTAL_DISTANCE,distance);
            DecimalFormat distanceFormater = (DecimalFormat) NumberFormat.getInstance();
            distanceFormater.setMinimumFractionDigits(2);
            distanceFormater.setMaximumFractionDigits(2);
            mFunctionFragment.showUpgradeDistance(distanceFormater.format(distance/1000.0));
        }
    }

    @Override
    public void startTrackActivity(BaseActivity activity) {
        BaseActivity host = activity;
        if(host == null){
            CFLog.e(this.getClass().getName(),"Activity has detached");
            return;
        }
        if(SystemUtils.isGpsEnabled(GlobalApplication.getAppContext())){
            Intent intent = new Intent(host, MovementTrackActivity.class);
            host.startActivity(intent);
        }
        else{
            host.showSettingDialog(Settings.ACTION_LOCATION_SOURCE_SETTINGS, host.getString(R.string.need_gps)).show();
        }
    }
}
