package com.stdnull.runmap.model;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.utils.StringUtils;

/**
 * 数据库存储数据实体类
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
        if( AMapUtils.calculateLineDistance(latLng,that.latLng) < RMConfiguration.MAX_DISTANCE){
            return true;
        }
        if(StringUtils.isEmpty(buildName) && StringUtils.isEmpty(that.buildName)){
            return false;
        }
        return buildName != null ? buildName.equals(that.buildName) : that.buildName == null;

    }

    @Override
    public String toString() {
        return "BuildingPoint{" +
                "latLng=" + latLng +
                ", buildName='" + buildName + '\'' +
                ", time=" + time +
                '}';
    }
}
