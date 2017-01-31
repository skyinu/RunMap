package com.stdnull.runmap.map;

import com.amap.api.maps.model.LatLng;

/**
 * Created by chen on 2017/1/28.
 */

public interface OnDistanceIncreasedListener {
    void onDistanceIncreased(float distance, LatLng latLng);
}
