package com.stdnull.runmap.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.AppManager;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.map.AmLocationManager;
import com.stdnull.runmap.map.OnDistanceIncreasedListener;
import com.stdnull.runmap.map.OnGpsPowerListener;
import com.stdnull.runmap.map.OnGpsSwitchListener;
import com.stdnull.runmap.utils.SystemUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TrackActivity extends BaseActivity implements View.OnClickListener,
        OnDistanceIncreasedListener,OnGpsPowerListener,AppManager.AppStateListener,OnGpsSwitchListener{
    private AMap mAmap;//地图交互对象对象
    private RelativeLayout mMapUiContainer;
    private Button mBtnChangeMapStyle;
    private Button mBtnQuitMapUI;

    private View mGpsLevelView;
    private Button mChangeToMapUI;
    private TextView mDataUIDistance;
    private TextView mDataUITime;

    private TextView mTvDurationDistance;
    private TextView mTvDurationTime;
    private long mStartTime;
    private long mDurationDistance = 0;

    private boolean time_update_flag = false;

    private DecimalFormat mDistanceFormater;
    DecimalFormat mTimeFormater;


    private AlertDialog mTipsDialog = null;
    private static final String KEY_START_TIME = "START_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        initView();
        initConfig();
        AmLocationManager.getInstance().initAMap(mAmap,0);
        AmLocationManager.getInstance().setDistanceListener(this);
        AmLocationManager.getInstance().setGpsPowerListener(this);
        AmLocationManager.getInstance().setGpsSwitchListener(this);
        AppManager.getInstance().registerListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AmLocationManager.getInstance().startLocation();
    }

    protected void initView() {
        mAmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMapUiContainer = (RelativeLayout) findViewById(R.id.map_ui_group);
        mBtnChangeMapStyle = (Button) findViewById(R.id.btn_change_map_style);
        mBtnQuitMapUI = (Button) findViewById(R.id.btn_quit_map);

        mTvDurationDistance = (TextView) findViewById(R.id.tv_duration_distance);
        mTvDurationTime = (TextView) findViewById(R.id.tv_duration_time);
        mBtnChangeMapStyle.setOnClickListener(this);
        mBtnQuitMapUI.setOnClickListener(this);

        mGpsLevelView = findViewById(R.id.gps_level);
        mChangeToMapUI = (Button) findViewById(R.id.change_to_map_ui);
        mDataUIDistance = (TextView) findViewById(R.id.data_ui_distance);
        mDataUITime = (TextView) findViewById(R.id.data_ui_time);
        mGpsLevelView.setOnClickListener(this);
        mChangeToMapUI.setOnClickListener(this);
    }

    protected void initConfig() {
        mStartTime = SystemClock.elapsedRealtime();
        mDistanceFormater = (DecimalFormat) NumberFormat.getInstance();
        mDistanceFormater.setMinimumFractionDigits(2);
        mDistanceFormater.setMaximumFractionDigits(2);

        mTimeFormater = (DecimalFormat) DecimalFormat.getInstance();
        mTimeFormater.applyPattern("00");

        mStartTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_map_style:
                changeMapStyle();
                break;
            case R.id.btn_quit_map:
                startScaleOut();
                break;
            case R.id.change_to_map_ui:
                startScaleIn();
                break;
        }
    }

    public void startScaleIn(){
        ( getFragmentManager().findFragmentById(R.id.map)).setUserVisibleHint(true);
        Animation animation = AnimationUtils.loadAnimation(TrackActivity.this,R.anim.scale_in);
        mMapUiContainer.startAnimation(animation);
    }

    private void startScaleOut(){

        Animation animation = AnimationUtils.loadAnimation(TrackActivity.this,R.anim.scale_out);
        mMapUiContainer.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                (getFragmentManager().findFragmentById(R.id.map)).setUserVisibleHint(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void changeMapStyle() {
        if (mAmap.getMapType() == AMap.MAP_TYPE_NORMAL) {
            mBtnChangeMapStyle.setBackgroundResource(R.mipmap.map_style_senior);
        } else {
            mBtnChangeMapStyle.setBackgroundResource(R.mipmap.map_style_normal);
        }
        AmLocationManager.getInstance().changeMapStyle();
    }

    @Override
    public void onDistanceIncreased(float distance, LatLng latLng) {
        CFLog.e(this.getClass().getName(), "current distance =" + mDurationDistance + " added distance=" + distance);
        mDurationDistance += distance;
        String distanceText = mDistanceFormater.format(mDurationDistance / 1000.0);

        mTvDurationDistance.setText(distanceText);
        mDataUIDistance.setText(distanceText);
    }

    @Override
    public void onGpsPower(int powerStatus) {
        if(powerStatus == AMapLocation.GPS_ACCURACY_GOOD){
            ((LevelListDrawable)mGpsLevelView.getBackground()).setLevel(3);
        }
        else{
            ((LevelListDrawable)mGpsLevelView.getBackground()).setLevel(1);
        }
    }

    @Override
    public void onGPSSwitchChanged() {
        CFAsyncTask<Boolean> task = new CFAsyncTask<Boolean>() {
            @Override
            public Boolean onTaskExecuted(Object... params) {
                return SystemUtils.isGpsEnabled(GlobalApplication.getAppContext());
            }

            @Override
            public void onTaskFinished(Boolean result) {
                if(result == false && (mTipsDialog == null || !mTipsDialog.isShowing())){
                    mTipsDialog = showSettingDialog(Settings.ACTION_LOCATION_SOURCE_SETTINGS, getString(R.string.gps_closed_tips));
                    mTipsDialog.show();
                }
            }
        };
        TaskHanler.getInstance().sendTaskDelayed(task,2000);
    }


    public void updateTime() {

        CFAsyncTask task = new CFAsyncTask<String>() {

            @Override
            public String onTaskExecuted(Object... params) {
                long start = (long) params[0];
                double duration = (SystemClock.elapsedRealtime() - start)/1000.0;
                String second = mTimeFormater.format(duration%60);
                duration /= 60;
                String minute = mTimeFormater.format(duration%60);
                duration /= 60;
                String hour = mTimeFormater.format(duration%60);
                return hour+":"+minute+":"+second;
            }

            @Override
            public void onTaskFinished(String result) {
                mTvDurationTime.setText(result);
                mDataUITime.setText(result);
                updateTime();
            }
        };
        TaskHanler.getInstance().sendTaskDelayed(task,1000,mStartTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!time_update_flag){
            time_update_flag = true;
            updateTime();

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.string_msg_exit_hint);
        builder.setNegativeButton(R.string.string_no,null);
        builder.setPositiveButton(R.string.string_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataManager.getInstance().saveDataAndClearMemory();
                finish();
            }
        });
        builder.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AmLocationManager.getInstance().onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStartTime = savedInstanceState.getLong(KEY_START_TIME);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_START_TIME, mStartTime);
    }


    @Override
    public void onForeground(Context context) {

    }

    @Override
    public void onBackground(Context context) {
        DataManager.getInstance().saveDataAndClearMemory();
    }

}
