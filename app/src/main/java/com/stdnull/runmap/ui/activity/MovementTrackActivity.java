package com.stdnull.runmap.ui.activity;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.stdnull.runmap.R;
import com.stdnull.runmap.model.IMoveTrack;
import com.stdnull.runmap.model.MoveTrackModel;
import com.stdnull.runmap.modules.map.AmapWrapper;
import com.stdnull.runmap.presenter.action.ITrackPresenter;
import com.stdnull.runmap.presenter.actionImpl.TrackPresenterImpl;
import com.stdnull.runmap.ui.uibehavior.IMovementTrackActivity;

/**
 * Created by chen on 2017/6/3.
 */

public class MovementTrackActivity extends BaseActivity implements IMovementTrackActivity, View.OnClickListener{
    /**
     * 显示地图的Layout
     */
    private RelativeLayout mRlMapUiContainer;
    private Button mBtnMapStyleChange;
    private Button mBtnActivityLayoutChange;
    private TextView mTvMoveDistance;
    private TextView mTvMoveDuration;

    /**
     * 概要数据显示页Layout
     */
    private View mViewGpsPower;
    private Button mBtnChangeMapUi;
    private TextView mTvDataMoveDistance;
    private TextView mTvDataMoveTime;

    private IMoveTrack mMoveTrackModel;
    private ITrackPresenter mTrackPresenter;

    private PopupWindow mSharePopWindow;

    private SwipeRefreshLayout mLocationProgressLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        bindView();
        init();
    }

    protected void init() {
        AMap amap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        AmapWrapper amapWrapper = new AmapWrapper(amap);
        mMoveTrackModel = new MoveTrackModel();
        mTrackPresenter = new TrackPresenterImpl(this, mMoveTrackModel, amapWrapper);
        //初始化地图
        mTrackPresenter.initAmap(this);
        mTrackPresenter.registerWXShareAPI(this);
        mTrackPresenter.startForeInfoService(this);

    }

    protected void bindView(){
        //初始化啊Map UIi相关
        mRlMapUiContainer = (RelativeLayout) findViewById(R.id.rl_map_ui_group);
        mBtnMapStyleChange = (Button) findViewById(R.id.btn_change_map_style);
        mBtnActivityLayoutChange = (Button) findViewById(R.id.btn_quit_map_ui);
        mTvMoveDistance = (TextView) findViewById(R.id.tv_duration_distance);
        mTvMoveDuration = (TextView) findViewById(R.id.tv_duration_time);

        mBtnMapStyleChange.setOnClickListener(this);
        mBtnActivityLayoutChange.setOnClickListener(this);

        mLocationProgressLayout = (SwipeRefreshLayout) findViewById(R.id.spl_refresh_view);
        mLocationProgressLayout.setRefreshing(true);
        mLocationProgressLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTrackPresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTrackPresenter.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        mTrackPresenter.onBackPressed(this);
    }

    @Override
    public void updateDistance(String distanceGap) {
        mTvMoveDistance.setText(distanceGap);
        if(mTvDataMoveDistance != null) {
            mTvDataMoveDistance.setText(distanceGap);
        }
    }

    @Override
    public void updateTime(String time) {
        if(mTvDataMoveTime != null) {
            mTvDataMoveTime.setText(time);
        }
        mTvMoveDuration.setText(time);
    }

    @Override
    public void updateGpsPower(int gpsPower) {
        if(mViewGpsPower ==null){
            return;
        }
        if(gpsPower == AMapLocation.GPS_ACCURACY_GOOD){
            mViewGpsPower.getBackground().setLevel(3);
        }
        else{
            mViewGpsPower.getBackground().setLevel(1);
        }
    }

    @Override
    public AlertDialog showGpsSettingDialog(String action, int msgId) {
        return showSettingDialog(action, getString(msgId));
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showExitHintLayout() {
        findViewById(R.id.rl_map_btn_container).setVisibility(View.GONE);
        findViewById(R.id.ll_text_container).setVisibility(View.GONE);
        RelativeLayout trackExitHint = (RelativeLayout) findViewById(R.id.rl_track_exit_hint_container);
        trackExitHint.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        mTrackPresenter.showMapUiLayout(MovementTrackActivity.this, mRlMapUiContainer, getFragmentManager().findFragmentById(R.id.map));
        mTrackPresenter.scaleCurrentCamera();
    }

    @Override
    public void dismissRefresh() {
        mLocationProgressLayout.setRefreshing(false);
        mLocationProgressLayout.setEnabled(false);
        //for experience, init layout here
        inflateDataUiLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_map_style:
                changeMapType();
                break;
            case R.id.btn_quit_map_ui:
                mTrackPresenter.showDataUiLayout(MovementTrackActivity.this, mRlMapUiContainer,
                        getFragmentManager().findFragmentById(R.id.map));
                break;
            case R.id.btn_change_to_map_ui:
                mTrackPresenter.showMapUiLayout(MovementTrackActivity.this, mRlMapUiContainer,
                        getFragmentManager().findFragmentById(R.id.map));
                break;
            case R.id.btn_finish:
                finish();
                break;
            case R.id.btn_share:
                showShareLayout();
                break;
            case R.id.btn_share_to_circle:
            case R.id.btn_share_to_friend:
                mSharePopWindow.dismiss();
                mTrackPresenter.share(v.getId());
                break;
        }
    }

    private void inflateDataUiLayout(){
        ViewStub viewStub = (ViewStub) findViewById(R.id.vs_data_ui_layout);
        if(viewStub == null){
            //already inflate
            return;
        }
        View dataUiRoot = viewStub.inflate();
        //初始化数据UI相关
        mViewGpsPower = dataUiRoot.findViewById(R.id.view_gps_power);
        mBtnChangeMapUi = (Button) dataUiRoot.findViewById(R.id.btn_change_to_map_ui);
        mTvDataMoveDistance = (TextView) dataUiRoot.findViewById(R.id.tv_data_ui_distance);
        mTvDataMoveTime = (TextView) dataUiRoot.findViewById(R.id.tv_data_ui_time);
        mBtnChangeMapUi.setOnClickListener(this);
    }

    private void showShareLayout() {
        mSharePopWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this).inflate(R.layout.sharelayout, null);
        view.findViewById(R.id.btn_share_to_circle).setOnClickListener(this);
        view.findViewById(R.id.btn_share_to_friend).setOnClickListener(this);
        mSharePopWindow.setContentView(view);
        mSharePopWindow.setFocusable(false);
        mSharePopWindow.setBackgroundDrawable(new BitmapDrawable());
        mSharePopWindow.setOutsideTouchable(true);
        mSharePopWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM,0,0);
        return;
    }

    /**
     * 修改地图显示样式,目前支持2D地图及卫星地图
     */
    private void changeMapType() {
        if (mTrackPresenter.getMapType() == AMap.MAP_TYPE_NORMAL) {
            mBtnMapStyleChange.setBackgroundResource(R.mipmap.map_style_senior);
        } else {
            mBtnMapStyleChange.setBackgroundResource(R.mipmap.map_style_normal);
        }
        mTrackPresenter.changeMapType();
    }
}
