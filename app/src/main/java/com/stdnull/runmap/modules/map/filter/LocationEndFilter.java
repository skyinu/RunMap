package com.stdnull.runmap.modules.map.filter;

import com.amap.api.location.AMapLocation;

/**
 * Created by chen on 2017/6/3.
 */

public class LocationEndFilter implements ILocationFilter {
    private boolean locationEnd = false;

    public LocationEndFilter(boolean locationEnd) {
        this.locationEnd = locationEnd;
    }

    @Override
    public boolean accept(AMapLocation previous, AMapLocation current) {
        return !locationEnd;
    }
}
