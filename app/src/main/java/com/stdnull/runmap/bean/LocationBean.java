package com.stdnull.runmap.bean;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/1/27.
 */

public class LocationBean {
    /**
     * 定位数据的经纬度,存储经纬度坐标值的类，单位角度。
     */
    private List<LatLng> mLatDatas;

    private List<TrackPoint> mPointDatas;


    public LocationBean(){
        mLatDatas = new ArrayList<>();
        mPointDatas = new ArrayList<>();
    }

    public List<TrackPoint> getPointDatas() {
        return mPointDatas;
    }

    public void addPointDatas(TrackPoint points) {
        mPointDatas.add(points);
    }

    public void addLatLng(LatLng latLng){
        mLatDatas.add(latLng);
    }
    public List<LatLng> getLatDatas(){
        return mLatDatas;
    }
}
