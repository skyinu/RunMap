package com.stdnull.runmap.bean;

import com.amap.api.maps.model.LatLng;

/**
 * Created by chen on 2017/1/31.
 */

public class BuildingPoint {
    private LatLng latLng;
    private String buildName;
    private long time;

    public BuildingPoint(LatLng latLng, String buildName) {
        this.latLng = latLng;
        this.buildName = buildName;
    }

    public LatLng getLatLng() {
        return latLng;
    }


    public String getBuildName() {
        return buildName;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuildingPoint that = (BuildingPoint) o;
        return buildName != null ? buildName.equals(that.buildName) : that.buildName == null;

    }
}
