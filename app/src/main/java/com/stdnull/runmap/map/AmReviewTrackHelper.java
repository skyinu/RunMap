package com.stdnull.runmap.map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.stdnull.runmap.bean.TrackPoint;
import com.stdnull.runmap.common.CFLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/1/30.
 */

public class AmReviewTrackHelper {
    private int currentIndex ;
    private List<List<LatLng>> mDataSource;
    private boolean firstShow = false;
    public void notifyNewData(Map<Integer, List<TrackPoint>> source){
        AmLocationManager.getInstance().clearAll();
        List<List<LatLng>> showData = prepareShow(source);
        this.mDataSource = showData;
        currentIndex = 0;
        //解决视图不变化的问题
        if(!firstShow) {
            firstShow = true;
            AmLocationManager.getInstance().moveToSpecficCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(showData.get(currentIndex).get(0), 16, 0, 0)));
        }
        AmLocationManager.getInstance().drawTrackLine(showData.get(currentIndex),currentIndex,new TrackLineMoveListener());

    }
    private List<List<LatLng>> prepareShow(Map<Integer,List<TrackPoint>> source){
        int count = source.keySet().size();
        List<List<LatLng>> groupData = new ArrayList<>();
        for(int i=0;i<count;i++) {
            List<LatLng> points = new ArrayList<>();
            List<TrackPoint> trackPointList = source.get(i);
            for (int j = 0; j < trackPointList.size(); j++) {
                points.add(trackPointList.get(j).getLocation());
            }
            groupData.add(points);
        }
        return groupData;
    }

//    @Override
//    public void move(double v) {
//        if(v == 0 && currentIndex < mDataSource.size()){
//            CFLog.e(AmLocationManager.TAG,"a line finished, prepare to draw new line");
//            currentIndex ++;
//            AmLocationManager.getInstance().drawTrackLine(mDataSource.get(currentIndex),currentIndex,AmReviewTrackHelper.this);
//        }
//    }

    class  TrackLineMoveListener implements SmoothMoveMarker.MoveListener{
        private boolean hasExecuted = false;

        @Override
        public void move(double v) {
            if(!hasExecuted && v == 0 && currentIndex < mDataSource.size()){
                CFLog.e(AmLocationManager.TAG,"a line finished, prepare to draw new line");
                hasExecuted = true;
                currentIndex ++;
                AmLocationManager.getInstance().drawTrackLine(mDataSource.get(currentIndex),currentIndex,new TrackLineMoveListener());
            }
        }
    }
}
