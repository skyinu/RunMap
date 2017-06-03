package com.stdnull.runmap.modules.map;

import android.app.Activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.stdnull.runmap.model.BuildingPoint;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.modules.map.filter.ILocationFilter;
import com.stdnull.runmap.modules.map.listenter.IGpsPowerListener;
import com.stdnull.runmap.modules.map.listenter.IGpsSwicthListener;
import com.stdnull.runmap.modules.map.listenter.IMapCaptureFinished;
import com.stdnull.runmap.modules.map.listenter.IOnNewLocation;

import java.util.List;

/**
 * Created by chen on 2017/5/31.
 */

public interface IMap {
    void initMap();
    void startLocation(Activity context);
    void setMinZoomLevel(int level);
    void addLocationFilter(ILocationFilter filter);
    boolean removeLocationFilter(ILocationFilter filter);
    void setOnGpsPowerListener(IGpsPowerListener listener);
    void setOnGpsSwitchListener(IGpsSwicthListener listener);
    void setOnNewLocationListener(IOnNewLocation listener);
    void requestRegeoAddress(AMapLocation aMapLocation, final TrackPoint trackPoint);
    AMap getController();
    int getMapType();
    void changeMapType();
    void moveToSpecficCamera(CameraUpdate cameraUpdate);
    void captureMap(IMapCaptureFinished callback);
    void drawPolyLine(float speed, LatLng... latLngs);
    void drawPolyLine(List<LatLng> latLngs, int color);
    void drawTrackAnimation(List<LatLng> drawSource, int currentIndex, SmoothMoveMarker.MoveListener moveListener);
    void clear();
    void drawMarkers(List<BuildingPoint> buildingPointList);
}
