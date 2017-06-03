package com.stdnull.runmap.modules.map.filter;

import android.os.SystemClock;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.common.RMConfiguration;

/**
 * Created by chen on 2017/6/3.
 */

public class SpeedAndDistanceFilter implements ILocationFilter {
    private long mPreviousUpdateTime;
    @Override
    public boolean accept(AMapLocation previous, AMapLocation current) {
        if(previous == null){
            return true;
        }
        LatLng pre = new LatLng(previous.getLatitude(), previous.getLongitude());
        LatLng cur = new LatLng(current.getLatitude(), current.getLongitude());
        float distance = AMapUtils.calculateLineDistance(pre, cur);
        if(distance < RMConfiguration.DRAW_DISTANCE){
            return false;
        }
        float speed = current.getSpeed();
        double interval = (SystemClock.elapsedRealtime() - mPreviousUpdateTime)/1000.0;
        float v = (float) (distance/interval);
        if(v > RMConfiguration.MAX_SPEED || v > speed * 1.5){
            return false;
        }
        mPreviousUpdateTime = SystemClock.elapsedRealtime();
        return true;
    }
}
