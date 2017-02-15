package com.stdnull.runmap.map;

import android.database.ContentObserver;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.utils.SystemUtils;

/**
 * 接口实现类
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
        GlobalApplication.getAppContext().getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
    }
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        CFLog.i(AmLocationManager.TAG,"activate");
        this.mLocationChangedListener = onLocationChangedListener;
        mStateListner.notifyServiceActive();

    }

    @Override
    public void deactivate() {
        CFLog.i(AmLocationManager.TAG,"deactivate");
        mStateListner.notifyServiceDeactivate();
        mLocationChangedListener = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        CFLog.i(AmLocationManager.TAG,"onLocationChanged="+aMapLocation.toString());
        if(mLocationChangedListener != null && aMapLocation != null && aMapLocation.getErrorCode() == 0){
            mStateListner.notifyLocationChanged(mLocationChangedListener,aMapLocation);
        }

    }

    @Override
    public void onMapLoaded() {
        mStateListner.notifyMapLoaded();
    }

    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mStateListner.notifyGPSSwitchChanged();
        }
    };
}
