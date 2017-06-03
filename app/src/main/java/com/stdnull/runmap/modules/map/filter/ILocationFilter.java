package com.stdnull.runmap.modules.map.filter;

import com.amap.api.location.AMapLocation;

/**
 * Created by chen on 2017/6/3.
 */

public interface ILocationFilter {
    boolean accept(AMapLocation previous, AMapLocation current);
}
