package com.stdnull.runmap.modules.map;

import android.database.ContentObserver;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.common.CFLog;

/**
 * 接口实现类
 * Created by chen on 2017/1/26.
 */

class AMapStateListenerImpl implements LocationSource,AMapLocationListener,AMap.OnMapLoadedListener {
    /**
     * SDK自身的位置变化监听对象
     */
    private OnLocationChangedListener mLocationChangedListener;

    private AMapStateListener mStateListener;
    public AMapStateListenerImpl(){
    }

    public void setStateListener(@NonNull AMapStateListener stateListener) {
        this.mStateListener = stateListener;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        CFLog.i("TAG","activate");
        this.mLocationChangedListener = onLocationChangedListener;
        mStateListener.notifyServiceActive();
        GlobalApplication.getAppContext().getContentResolver()
                .registerContentObserver(Settings.Secure.getUriFor(android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);

    }

    @Override
    public void deactivate() {
        CFLog.i("TAG","deactivate");
        mStateListener.notifyServiceDeactivate();
        mLocationChangedListener = null;
        mStateListener = null;
        GlobalApplication.getAppContext().getContentResolver()
                .unregisterContentObserver(mGpsMonitor);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        CFLog.i("TAG","onLocationChanged="+aMapLocation.toString());
        if(mLocationChangedListener != null && aMapLocation != null && aMapLocation.getErrorCode() == 0){
            mStateListener.notifyLocationChanged(mLocationChangedListener,aMapLocation);
        }

    }

    @Override
    public void onMapLoaded() {
        mStateListener.notifyMapLoaded();
    }

    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mStateListener.notifyGPSSwitchChanged();
        }
    };
}
