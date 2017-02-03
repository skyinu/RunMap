package com.stdnull.runmap.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.LocationSource;

public interface LocationStateListener{
    void notifyServiceActive();

    void notifyServiceDeactivate();

    void notifyLocationChanged(LocationSource.OnLocationChangedListener amListener, AMapLocation aMapLocation);

    void notifyMapLoaded();

    void notifyGPSSwitchChanged();
}