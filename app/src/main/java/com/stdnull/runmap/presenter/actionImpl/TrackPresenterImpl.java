package com.stdnull.runmap.presenter.actionImpl;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.lifecircle.AppStateListener;
import com.stdnull.runmap.lifecircle.LifeCycleMonitor;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.model.IMoveTrack;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.modules.map.IMap;
import com.stdnull.runmap.modules.map.filter.LocationEndFilter;
import com.stdnull.runmap.modules.map.filter.LocationTypeFilter;
import com.stdnull.runmap.modules.map.filter.SpeedAndDistanceFilter;
import com.stdnull.runmap.modules.map.listenter.IGpsPowerListener;
import com.stdnull.runmap.modules.map.listenter.IGpsSwicthListener;
import com.stdnull.runmap.modules.map.listenter.IMapCaptureFinished;
import com.stdnull.runmap.modules.map.listenter.IOnNewLocation;
import com.stdnull.runmap.presenter.action.ITrackPresenter;
import com.stdnull.runmap.service.MoveHintForeService;
import com.stdnull.runmap.ui.uibehavior.IMovementTrackActivity;
import com.stdnull.runmap.utils.ShareUtils;
import com.stdnull.runmap.utils.SystemUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by chen on 2017/6/3.
 */

public class TrackPresenterImpl implements ITrackPresenter, IOnNewLocation, IGpsSwicthListener,
        IGpsPowerListener, AppStateListener {
    private IMovementTrackActivity mMovementTrackActivity;
    private IMoveTrack mMoveModel;

    private IMap mapObject;
    private IWXAPI mWxApi;

    /**
     * 定位坐标有效性过滤
     */
    private LocationTypeFilter mLocationTypeFiler;

    private DecimalFormat mDistanceFormater;//格式化距离格式,只保留两位小数
    private DecimalFormat mTimeFormater;//格式化时间格式,00:00:00

    /**
     * 计时器
     */
    private Handler mSecondTimer = new SecondTimer();
    private static final int TIME_UPDATE_FREQUENCY = 1000;
    /**
     * 标记是否已开始计时
     */
    private boolean isTimerStarted = false;

    /**
     * 用于提示GPS开关信息的dialog
     */
    private AlertDialog mGpsTipsDialog;

    /**
     * 指示当前显示的Layout
     */
    private boolean isMapFragmentShowing = true;

    /**
     * 是否强制退出
     */
    private boolean isForceExit = false;

    private Messenger mServiceMessager;

    public TrackPresenterImpl(){
        mDistanceFormater = (DecimalFormat) NumberFormat.getInstance();
        mDistanceFormater.setMinimumFractionDigits(2);
        mDistanceFormater.setMaximumFractionDigits(2);
        mTimeFormater = (DecimalFormat) DecimalFormat.getInstance();
        mTimeFormater.applyPattern("00");
    }

    public TrackPresenterImpl(IMovementTrackActivity movementTrackActivity, IMoveTrack modeModel, IMap map) {
        this();
        this.mMovementTrackActivity = movementTrackActivity;
        this.mMoveModel = modeModel;
        this.mapObject = map;
    }

    @Override
    public void initAmap(Activity context) {
        mapObject.initMap();
        mapObject.setMinZoomLevel(8);
        mLocationTypeFiler = new LocationTypeFilter();
        mapObject.addLocationFilter(mLocationTypeFiler);
        mapObject.addLocationFilter(new SpeedAndDistanceFilter());
        mapObject.startLocation(context);
        //set listener
        mapObject.setOnNewLocationListener(this);
        mapObject.setOnGpsPowerListener(this);
        mapObject.setOnGpsSwitchListener(this);
    }

    @Override
    public void registerWXShareAPI(Context context) {
        mWxApi = WXAPIFactory.createWXAPI(context, RMConfiguration.WEIXIN_APP_ID,true);
        CFLog.e("Share","register = " + mWxApi.registerApp(RMConfiguration.WEIXIN_APP_ID));
    }

    @Override
    public int getMapType() {
        return mapObject.getMapType();
    }

    @Override
    public void changeMapType() {
        mapObject.changeMapType();
    }

    @Override
    public void showMapUiLayout(Context context, RelativeLayout mapUI, Fragment mapFrag) {
        if(isMapFragmentShowing){
            return;
        }
        mapFrag.setUserVisibleHint(true);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_in);
        mapUI.startAnimation(animation);
        isMapFragmentShowing = true;
    }

    @Override
    public void showDataUiLayout(Context context, RelativeLayout mapUI, final Fragment mapFrag) {
        if(!isMapFragmentShowing){
            return;
        }
        isMapFragmentShowing = false;
        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.scale_out);
        mapUI.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mapFrag.setUserVisibleHint(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBackPressed(final Context context) {
        if(isForceExit){
            mMovementTrackActivity.finishActivity();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.string_msg_exit_hint);
        builder.setNegativeButton(R.string.string_no,null);
        builder.setPositiveButton(R.string.string_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean shouldShowShare = mMoveModel.getHistoryCoordiates().size() > RMConfiguration.MIN_CACHE_DATA;
                mMoveModel.saveModelToDatabase(true);
                if(!shouldShowShare) {
                    mMovementTrackActivity.finishActivity();
                }
                else{
                    isForceExit = true;
                    mapObject.addLocationFilter(new LocationEndFilter(true));
                    mMovementTrackActivity.showExitHintLayout();
                }
            }
        });
        builder.show();

    }

    @Override
    public void scaleCurrentCamera() {
        CameraPosition position = mapObject.getController().getCameraPosition();
        List<TrackPoint> trackPoints = mMoveModel.getHistoryCoordiates();
        LatLng start = new LatLng(trackPoints.get(0).getLatitude(), trackPoints.get(0).getLongitude());
        LatLng end = new LatLng(trackPoints.get(trackPoints.size() - 1).getLatitude(), trackPoints.get(trackPoints.size() - 1).getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(new LatLngBounds(start, end), (int) (position.zoom - 6));
        mapObject.moveToSpecficCamera(update);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        mMoveModel.onRestoreInstanceState(bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        mMoveModel.onSaveInstanceState(bundle);
    }

    @Override
    public void share(final int shareId) {
        mapObject.captureMap(new IMapCaptureFinished() {
            @Override
            public void onMapCaptureFinished(Bitmap bitmap, int status) {
                WXImageObject imageObject = new WXImageObject(bitmap);

                WXMediaMessage mediaMessage = new WXMediaMessage();
                mediaMessage.mediaObject = imageObject;
                Bitmap thumb = Bitmap.createScaledBitmap(bitmap,50,50,true);
                bitmap.recycle();
                mediaMessage.thumbData = ShareUtils.bmpToByteArray(thumb,true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = ShareUtils.buildTransaction("img");
                req.message = mediaMessage;
                if(shareId == R.id.btn_share_to_friend){
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                }
                else{
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                CFLog.e("Share","send = "+ mWxApi.sendReq(req));
            }
        });
    }

    @Override
    public void startForeInfoService(Context context) {
        Intent intent = new Intent(context, MoveHintForeService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mServiceMessager = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceMessager = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onActicityCreated() {
        LifeCycleMonitor.getInstance().registerListener(this);
    }

    @Override
    public void onActivityDestoryed() {
        LifeCycleMonitor.getInstance().unRegisterListener(this);
    }


    @Override
    public void onNewLocation(AMapLocation location) {
        if(mLocationTypeFiler != null && mapObject.removeLocationFilter(mLocationTypeFiler)) {
            mLocationTypeFiler = null;
            //locate succeed, dismiss refresh layout
            mMovementTrackActivity.dismissRefresh();
        }
        if(!isTimerStarted){
            isTimerStarted = true;
            mSecondTimer.sendEmptyMessageDelayed(0, TIME_UPDATE_FREQUENCY);
        }
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        //新坐标点
        TrackPoint trackPoint = new TrackPoint(cur, SystemClock.elapsedRealtime());
        mapObject.requestRegeoAddress(location, trackPoint);

        List<TrackPoint> trackPoints = mMoveModel.getHistoryCoordiates();
        if(trackPoints.size() >= 1) {
            mapObject.drawPolyLine(location.getSpeed(), trackPoint.getLocation(),
                    trackPoints.get(trackPoints.size() - 1).getLocation());
        }

        float distance = mMoveModel.onNewLocation(trackPoint);
        String distanceText = mDistanceFormater.format(distance / 1000.0);
        mMovementTrackActivity.updateDistance(distanceText);

        Message msg = new Message();
        msg.what = MoveHintForeService.MSG_DISTANCE_UPDATE;
        msg.obj = distanceText;
        sendMsgToForeService(msg);
    }

    private void sendMsgToForeService(Message msg){
        if(mServiceMessager == null){
            return;
        }
        try {
            mServiceMessager.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGpsPower(int powerStatus) {
        mMovementTrackActivity.updateGpsPower(powerStatus);
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
                if(result == false && (mGpsTipsDialog == null || !mGpsTipsDialog.isShowing())){
                    mGpsTipsDialog = mMovementTrackActivity.showGpsSettingDialog(Settings.ACTION_LOCATION_SOURCE_SETTINGS, R.string.gps_closed_tips);
                    mGpsTipsDialog.show();
                }
            }
        };
        //daley this message because sometime it can't show correctly
        TaskHanler.getInstance().sendTaskDelayed(task,2000);
    }

    @Override
    public void onForeground(Context context) {

    }

    @Override
    public void onBackground(Context context) {
        mMoveModel.saveModelToDatabase(false);
    }

    class SecondTimer extends Handler{
        @Override
        public void handleMessage(Message msg) {
            long duration = mMoveModel.updateDuration(TIME_UPDATE_FREQUENCY)/1000;
            String second = mTimeFormater.format(duration % 60);
            duration /= 60;
            String minute = mTimeFormater.format(duration % 60);
            duration /= 60;
            String hour = mTimeFormater.format(duration%60);
            String timeText = hour+":"+minute+":"+second;
            mMovementTrackActivity.updateTime(timeText);
            //I don't case message, only focus on frequency
            sendEmptyMessageDelayed(0, TIME_UPDATE_FREQUENCY);
            //update foreground service's info
            Message foreMsg = new Message();
            foreMsg.what = MoveHintForeService.MSG_TIME_UPDATE;
            foreMsg.obj = timeText;
            sendMsgToForeService(foreMsg);
        }
    }
}
