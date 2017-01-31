package com.stdnull.runmap.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.map.AmLocationManager;
import com.stdnull.runmap.map.OnDistanceIncreasedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TrackActivity extends BaseActivity implements View.OnClickListener, OnDistanceIncreasedListener {
    private AMap mAmap;//地图交互对象对象
    private RelativeLayout mMapUiContainer;
    private Button mBtnChangeMapStyle;
    private Button mBtnQuitMapUI;

    private TextView mTvDurationDistance;
    private TextView mTvDurationTime;
    private long mStartTime;
    private long mDurationDistance = 0;

    private boolean time_update_flag = false;

    private DecimalFormat mDistanceFormater;
    DecimalFormat mTimeFormater;

    private static final String KEY_START_TIME = "START_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        initView();
        initConfig();
        AmLocationManager.getInstance().initAMap(mAmap);
        AmLocationManager.getInstance().setDistanceListener(this);

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
                break;

        }
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

        mTvDurationDistance.setText(mDistanceFormater.format(mDurationDistance / 1000.0));
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
        DataManager.getInstance().cacheDataToDatabase();
        super.onStop();
        DataManager.getInstance().clearDataInMemory();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
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
}
