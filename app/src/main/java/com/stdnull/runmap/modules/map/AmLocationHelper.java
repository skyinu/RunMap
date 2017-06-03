package com.stdnull.runmap.modules.map;

import android.os.SystemClock;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;

import java.util.List;

/**
 * 地图数据处理帮助类
 * Created by chen on 2017/1/28.
 */

public class AmLocationHelper{
    private float mLatestIncreasedDistance = 0;
    private GeocodeSearch mGeocodeSearch;
    private Long mLatestUpdateTime;

    public AmLocationHelper(){
        mGeocodeSearch = new GeocodeSearch(GlobalApplication.getAppContext());
    }
    public boolean shouldAddLatLng(List<TrackPoint> trackPointList, LatLng latLng, float speed){
        if(trackPointList.isEmpty()) {
            mLatestUpdateTime = SystemClock.elapsedRealtime();
            CFLog.e("TAG","Add new data = " + latLng.toString());
            mLatestIncreasedDistance = 0;
            return true;
        }
        else{
            TrackPoint point = trackPointList.get(trackPointList.size()-1);
            float distance = AMapUtils.calculateLineDistance(latLng,new LatLng(point.getLatitude(),point.getLongitude()));
            if(distance < RMConfiguration.DRAW_DISTANCE){
                mLatestIncreasedDistance = 0;
                CFLog.e("TAG","to closed , don't need , distance = " + distance);
                return false;
            }
            if(!isErrorLaglng(distance,speed)){
                mLatestUpdateTime = SystemClock.elapsedRealtime();
                mLatestIncreasedDistance = distance;
                CFLog.i("TAG","Add new data = " + latLng.toString());
                return true;
            }
            return false;
        }
    }


    private boolean isErrorLaglng(float distance,float speed){
        double interval = (SystemClock.elapsedRealtime() - mLatestUpdateTime)/1000.0;
        float v = (float) (distance/interval);
        if(v > RMConfiguration.MAX_SPEED || v > speed * 1.5){
            CFLog.e("TAG","error data, don't need to add, speed ="+v +" ,"+speed);
            return true;
        }
        return false;

    }



    public RegeocodeAddress regeocodeAddress(LatLonPoint latLonPoint){
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        try {
            return mGeocodeSearch.getFromLocation(query);
        } catch (AMapException e) {
            CFLog.e(this.getClass().getName(),"Regeocode failed");
        }
        return null;
    }

    public int getColorBySpeed(float speed){
        float kmh = speed * 3.6F;
        float fraction = kmh/90;
        int color = evaluate(fraction,RMConfiguration.LOW_SPEED_COLOR,RMConfiguration.HIGH_SPEED_COLOR);
        return  color;
    }

    private int evaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int)(fraction * (endA - startA))) << 24) |
                ((startR + (int)(fraction * (endR - startR))) << 16) |
                ((startG + (int)(fraction * (endG - startG))) << 8) |
                ((startB + (int)(fraction * (endB - startB))));
    }

}
