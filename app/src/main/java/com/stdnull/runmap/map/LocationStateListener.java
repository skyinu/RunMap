package com.stdnull.runmap.map;

import com.amap.api.location.AMapLocation;

public interface LocationStateListener{
    void notifyServiceActive();

    void notifyServiceDeactivate();

    void notifyLocationChanged(AMapLocation aMapLocation);

    void notifyMapLoaded();
}