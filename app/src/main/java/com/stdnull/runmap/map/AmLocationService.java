package com.stdnull.runmap.map;

import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.stdnull.runmap.common.CFLog;

/**
 * Created by chen on 2017/1/26.
 */

public class AmLocationService implements LocationSource,AMapLocationListener,AMap.OnMapLoadedListener {
    /**
     * SDK自身的位置变化监听对象
     */
    protected OnLocationChangedListener mLocationChangedListener;

    protected LocationStateListener mStateListner;
    public AmLocationService(@NonNull LocationStateListener stateListener){
        super();
        this.mStateListner = stateListener;
    }
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        CFLog.i(this.getClass().getName(),"activate");
        this.mLocationChangedListener = onLocationChangedListener;
        mStateListner.notifyServiceActive();

    }

    @Override
    public void deactivate() {
        CFLog.i(this.getClass().getName(),"deactivate");
        mStateListner.notifyServiceDeactivate();
        mLocationChangedListener = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        CFLog.i(this.getClass().getName(),"onLocationChanged="+aMapLocation.toString());
        if(mLocationChangedListener != null && aMapLocation != null && aMapLocation.getErrorCode() == 0){
            mLocationChangedListener.onLocationChanged(aMapLocation);//显示系统定位蓝点
        }
        mStateListner.notifyLocationChanged(aMapLocation);
    }

    @Override
    public void onMapLoaded() {
        mStateListner.notifyMapLoaded();
    }
}
