package com.stdnull.runmap.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.common.ShareHelper;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.AppManager;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.map.AmLocationManager;
import com.stdnull.runmap.map.OnDistanceIncreasedListener;
import com.stdnull.runmap.map.OnGpsPowerListener;
import com.stdnull.runmap.map.OnGpsSwitchListener;
import com.stdnull.runmap.service.DataService;
import com.stdnull.runmap.utils.SystemUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 轨迹监控页面
 */
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

    private RelativeLayout mMapBtnLayout;
    private LinearLayout mTextContainer;

    private RelativeLayout mClosedBtnLayout;

    private long mStartTime;
    private long mDurationDistance = 0;

    private boolean time_update_flag = false;

    private DecimalFormat mDistanceFormater;
    DecimalFormat mTimeFormater;


    private AlertDialog mTipsDialog = null;
    private static final String KEY_START_TIME = "START_TIME";
    private static final String KEY_DURATION_DISTANCE = "DURATION_DISTANCE";

    private Messenger messenger = null;

    private boolean isClosed = false;
    private boolean isMapFragmentShowing = true;



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

        mMapBtnLayout = (RelativeLayout) findViewById(R.id.map_btn_container);
        mTextContainer = (LinearLayout) findViewById(R.id.text_container);


    }

    protected void initConfig() {
        mStartTime = SystemClock.elapsedRealtime();
        mDistanceFormater = (DecimalFormat) NumberFormat.getInstance();
        mDistanceFormater.setMinimumFractionDigits(2);
        mDistanceFormater.setMaximumFractionDigits(2);

        mTimeFormater = (DecimalFormat) DecimalFormat.getInstance();
        mTimeFormater.applyPattern("00");

        mStartTime = SystemClock.elapsedRealtime();

        Intent intent = new Intent(this, DataService.class);
        bindService(intent,new DataServiceConnection(),Context.BIND_AUTO_CREATE);

        ShareHelper.getInstance().initWXShare(this);

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
            case R.id.finish_btn:
                finish();
                break;
            case R.id.share_btn:
                ShareHelper.getInstance().showShareView(TrackActivity.this);
                break;
        }
    }



    public void startScaleIn(){
        ( getFragmentManager().findFragmentById(R.id.map)).setUserVisibleHint(true);
        Animation animation = AnimationUtils.loadAnimation(TrackActivity.this,R.anim.scale_in);
        mMapUiContainer.startAnimation(animation);
        isMapFragmentShowing = true;
    }

    private void startScaleOut(){
        isMapFragmentShowing = false;

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
        if(messenger != null){
            Message msg = new Message();
            msg.what = DataService.MSG_DISTANCE_UPDATE;
            msg.obj = distanceText;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
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
            public void onTaskFinished(String result)  {
                mTvDurationTime.setText(result);
                mDataUITime.setText(result);
                if(messenger != null){
                    Message msg = new Message();
                    msg.what = DataService.MSG_TIME_UPDATE;
                    msg.obj = result;
                    try {
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
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
        if(isClosed){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.string_msg_exit_hint);
        builder.setNegativeButton(R.string.string_no,null);
        builder.setPositiveButton(R.string.string_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataManager.getInstance().saveDataAndClearMemory(mDurationDistance,true);
                if(DataManager.getInstance().getTrackPoints().size() < RMConfiguration.MIN_CACHE_DATA) {
                    finish();
                }
                else{
                    prepareShare();
                }
            }
        });
        builder.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().unRegisterListener(this);
        AmLocationManager.getInstance().onDestroy();
        stopService(new Intent(this,DataService.class));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStartTime = savedInstanceState.getLong(KEY_START_TIME);
            mDurationDistance = savedInstanceState.getLong(KEY_DURATION_DISTANCE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_START_TIME, mStartTime);
        outState.putLong(KEY_DURATION_DISTANCE,mDurationDistance);
    }


    @Override
    public void onForeground(Context context) {

    }

    @Override
    public void onBackground(Context context) {
        DataManager.getInstance().saveDataAndClearMemory(mDurationDistance,false);
    }

    class DataServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
        }
    }


    private void prepareShare(){
        isClosed = true;
        AmLocationManager.getInstance().setClosed(true);
        mMapBtnLayout.setVisibility(View.GONE);
        mTextContainer.setVisibility(View.GONE);
        mClosedBtnLayout = (RelativeLayout) findViewById(R.id.closed_map_btn);
        mClosedBtnLayout.setVisibility(View.VISIBLE);
        findViewById(R.id.finish_btn).setOnClickListener(this);
        findViewById(R.id.share_btn).setOnClickListener(this);

        if(!isMapFragmentShowing){
            startScaleIn();
        }
        AmLocationManager.getInstance().scaleCurrentCamera();
    }
}
