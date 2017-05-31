package com.stdnull.runmap.model;

import com.amap.api.maps.model.LatLng;

/**
 * 回放轨迹的数据存储点
 * Created by chen on 2017/1/30.
 */

public class TrackPoint {
    private LatLng location;

    private String buildName;

    private long timeStamp;


    public TrackPoint(LatLng location, String buildName, long timeStamp) {
        this.location = location;
        this.buildName = buildName;
        this.timeStamp = timeStamp;
    }

    public TrackPoint(LatLng location, long timeStamp) {
        this.location = location;
        this.timeStamp = timeStamp;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public double getLatitude() {
        return location.latitude;
    }

    public double getLongitude() {
        return location.longitude;
    }

    public String getBuildName() {
        return buildName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public LatLng getLocation() {
        return location;
    }
}
