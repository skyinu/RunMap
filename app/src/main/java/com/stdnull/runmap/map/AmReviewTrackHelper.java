package com.stdnull.runmap.map;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.stdnull.runmap.bean.TrackPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/1/30.
 */

public class AmReviewTrackHelper implements SmoothMoveMarker.MoveListener{
    private int currentIndex ;
    private List<List<LatLng>> mDataSource;

    public void notifyNewData(Map<Integer,List<TrackPoint>> source){
        List<List<LatLng>> showData = prepareShow(source);
        this.mDataSource = showData;
        currentIndex = 0;
        AmLocationManager.getInstance().drawTrackLine(showData.get(currentIndex),this);

    }
    private List<List<LatLng>> prepareShow(Map<Integer,List<TrackPoint>> source){
        int count = source.keySet().size();
        List<List<LatLng>> groupData = new ArrayList<>();
        for(int i=0;i<count;i++) {
            List<LatLng> points = new ArrayList<>();
            List<TrackPoint> trackPointList = source.get(i);
            for (int j = 0; j < source.size(); j++) {
                points.add(trackPointList.get(j).getLocation());
            }
            groupData.add(points);
        }
        return groupData;
    }

    @Override
    public void move(double v) {
        if(v == 0 && currentIndex < mDataSource.size()){
            currentIndex ++;
            AmLocationManager.getInstance().drawTrackLine(mDataSource.get(currentIndex),this);
        }
    }
}
