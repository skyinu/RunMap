package com.stdnull.runmap.modules.map.filter;

import com.amap.api.location.AMapLocation;

/**
 * Created by chen on 2017/6/3.
 */

public class LocationTypeFilter implements ILocationFilter {
    @Override
    public boolean accept(AMapLocation previous, AMapLocation current) {
        return current.getLocationType() == AMapLocation.LOCATION_TYPE_GPS;
    }
}
