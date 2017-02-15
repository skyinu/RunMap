package com.stdnull.runmap.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.LocationSource;

/**
 * 地图生命周期、GPS开关检测、地图加载的接口回调
 */
public interface LocationStateListener{
    void notifyServiceActive();

    void notifyServiceDeactivate();

    void notifyLocationChanged(LocationSource.OnLocationChangedListener amListener, AMapLocation aMapLocation);

    void notifyMapLoaded();

    void notifyGPSSwitchChanged();
}