package com.stdnull.runmap.map;

import android.Manifest;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Pair;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.bean.BuildingPoint;
import com.stdnull.runmap.bean.TrackPoint;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.ActivityContextManager;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.managers.PermissionManager;
import com.stdnull.runmap.permission.PermissionCallBack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chen on 2017/1/23.
 */

public class AmLocationManager implements LocationStateListener {

    public static final String TAG = "location";

    private AMap mAmap;

    private static AmLocationManager mInstance;
    private AmLocationService mLocationService;
    /**
     * 定位服务类实例，提供单次定位、持续定位、最后位置相关功能。
     */
    private AMapLocationClient mAmapLocationClient;
    /**
     * 定位参数设置，通过该实例可以对定位的相关参数进行设置
     */
    private AMapLocationClientOption mAmapLocationOption;
    /**
     * 定位间隔时长
     */
    private long mLocationInterval = 5 * RMConfiguration.SECOND;

    /**
     * 外部监听类
     */
    private OnDistanceIncreasedListener mDistanceListener;

    /**
     * 帮助类，分解部分功能
     */
    private AmLocationHelper mLocationHelper;

    private AmLocationManager() {
        mLocationService = new AmLocationService(this);
        mLocationHelper = new AmLocationHelper();

    }

    public static synchronized AmLocationManager getInstance() {
        if (mInstance == null) {
            mInstance = new AmLocationManager();
        }
        return mInstance;
    }

    //***********************初始化相关*************************************

    private void initLocationClient() {
        mAmapLocationClient = new AMapLocationClient(GlobalApplication.getAppContext());
        mAmapLocationClient.setLocationListener(mLocationService);
        mAmapLocationClient.setLocationOption(mAmapLocationOption);
    }

    private void initLocationOptions() {
        mAmapLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mAmapLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mAmapLocationOption.setInterval(mLocationInterval);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mAmapLocationOption.setHttpTimeOut(RMConfiguration.HTTP_OUT_TIME);
        //开启缓存机制
        mAmapLocationOption.setLocationCacheEnable(false);
        //需要返回地址
        mAmapLocationOption.setNeedAddress(true);
        //当设置为true时，网络定位可以返回海拔、角度和速度
        mAmapLocationOption.setSensorEnable(true);

    }

    public void initAMap(AMap aMap) {
        this.mAmap = aMap;
        //照传入的CameraUpdate参数移动可视区域。
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(19));
        //设置定位资源。如果不设置此定位资源则定位按钮不可点击。
        aMap.setLocationSource(mLocationService);
        //显示室内地图
        aMap.showIndoorMap(true);
        //设置最大缩放等级
        aMap.setMinZoomLevel(8);
        //如果显示定位层，则界面上将出现定位按钮，如果未设置Location Source 则定位按钮不可点击。
        aMap.setMyLocationEnabled(true);
        //定位、移动到地图中心点，跟踪并根据方向旋转地图
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        //定位中心原点
        MyLocationStyle style = new MyLocationStyle();
        style.radiusFillColor(Color.TRANSPARENT);
        style.strokeWidth(0);
        aMap.setMyLocationStyle(style);
        //去除缩放按钮
        UiSettings settings = aMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
    }


    //***************************************************************

    //***********************数据设置相关*************************************
    public void setDistanceListener(OnDistanceIncreasedListener listener) {
        this.mDistanceListener = listener;
    }
    //*********************************************************************


    //***************************UI相关，绘制操作*****************************************

    public void drawPolyLine(LatLng... latLngs) {
        List<LatLng> tmp = Arrays.asList(latLngs);
        drawPolyLine(tmp);
    }
    public void drawPolyLine(List<LatLng> latLngs) {
        CFLog.e(TAG, "draw new Poly");
        PolylineOptions options = new PolylineOptions();
        options.addAll(latLngs);
        options.color(Color.BLUE);
        options.width(20);
        mAmap.addPolyline(options);

    }

    public void drawTrackLine(List<LatLng> points,SmoothMoveMarker.MoveListener moveListener) {
        //代表构成的一个矩形区域，由两点决定
        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 1));
        mAmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        //按照指定的经纬度数据和时间，平滑移动
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(mAmap);
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        LatLng drivePoint = points.get(0);
        //计算点到线的距离
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(30);
        //设置监听
        smoothMarker.setMoveListener(moveListener);
        // 开始滑动
        smoothMarker.startSmoothMove();
    }


    public void drawMarker(List<BuildingPoint> buildingPointList){
        for(int i = 0;i<buildingPointList.size();i++){
            BuildingPoint point = buildingPointList.get(i);
            MarkerOptions options =new MarkerOptions().position(point.getLatLng()).title(point.getBuildName());
            options.snippet("停留"+(point.getTime()/1000/60)+"分钟");
            final Marker marker = mAmap.addMarker(options);
            marker.setInfoWindowEnable(true);
            marker.showInfoWindow();
        }
    }



    public void changeMapStyle() {
        if (mAmap.getMapType() == AMap.MAP_TYPE_NORMAL) {
            mAmap.setMapType(AMap.MAP_TYPE_SATELLITE);
        } else {
            mAmap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * 发起定位
     */
    public void startLocation() {
        PermissionManager.getInstance().requestPermission(ActivityContextManager.getInstance().getLatestActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new PermissionCallBack() {
                    @Override
                    public void onAllPermissionGranted() {
                        if (mAmapLocationClient != null) {
                            mAmapLocationClient.startLocation();
                        }
                    }

                    @Override
                    public void onDenied() {

                    }
                });
    }


    /**
     * 获得新的定位地址时的处理，包括以下几步
     * 1、判断是否非法数据
     * 2、构造数据类型添加数据点
     * 3、绘制线段
     * 4、经纬度逆编码
     *
     * @param aMapLocation
     */
    private void resolveLocationChanged(AMapLocation aMapLocation) {
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude(), true);
        List<TrackPoint> trackPointList = DataManager.getInstance().getTrackPoints();
        //判断数据的合理性
        if (mLocationHelper.shouldAddLatLng(trackPointList, latLng, aMapLocation.getSpeed())) {
            //绘制轨迹
            if (trackPointList.size() > 0) {
                drawPolyLine(trackPointList.get(trackPointList.size() - 1).getLocation(), latLng);
            }
            //添加数据
            TrackPoint trackPoint = new TrackPoint(latLng, SystemClock.elapsedRealtime());
            DataManager.getInstance().addTrackPoint(trackPoint);
            if (mDistanceListener != null) {
                mDistanceListener.onDistanceIncreased(mLocationHelper.getLatestIncreasedDistance(), latLng);
            }
            //位置逆编码
            requestRegeoAddress(aMapLocation, trackPoint);
        }

    }

    private void requestRegeoAddress(AMapLocation aMapLocation, final TrackPoint trackPoint) {
        CFAsyncTask<RegeocodeAddress> task = new CFAsyncTask<RegeocodeAddress>() {
            @Override
            public RegeocodeAddress onTaskExecuted(Object... params) {
                return mLocationHelper.regeocodeAddress((LatLonPoint) params[0]);
            }

            @Override
            public void onTaskFinished(RegeocodeAddress result) {
                if (result == null) {
                    return;
                }
                trackPoint.setBuildName(result.getBuilding());
                CFLog.e(TAG, "regeocode result = " + result.getDistrict());
            }
        };
        LatLonPoint point = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        TaskHanler.getInstance().sendTask(task, point);
    }

    //************************事件通知相关*********************************
    @Override
    public void notifyServiceActive() {
        if (mAmapLocationClient != null) {
            return;
        }
        initLocationOptions();
        initLocationClient();
    }

    @Override
    public void notifyServiceDeactivate() {
        if (mAmapLocationClient != null) {
            mAmapLocationClient.stopLocation();
            mAmapLocationClient.onDestroy();
        }
        mAmapLocationClient = null;
    }

    @Override
    public void notifyLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            resolveLocationChanged(aMapLocation);
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            CFLog.e(TAG, errText);
        }
    }

    @Override
    public void notifyMapLoaded() {

    }

    //**********************************************************




}
