package com.stdnull.runmap.modules.map;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.common.CFLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 轨迹播放辅助类
 * Created by chen on 2017/1/30.
 */

public class AmReviewTrackHelper {
    private int currentIndex ;
    private List<List<LatLng>> mDataSource;
    private boolean firstShow = false;
    public void notifyNewData(Map<Integer, List<TrackPoint>> source){
        AMapImpl.getInstance().clearAll();
        List<List<LatLng>> showData = prepareShow(source);
        this.mDataSource = showData;
        currentIndex = 0;
        //解决视图不变化的问题
        if(!firstShow) {
            firstShow = true;
            AMapImpl.getInstance().moveToSpecficCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(showData.get(currentIndex).get(0), 16, 0, 0)));
        }
        AMapImpl.getInstance().drawTrackLine(showData.get(currentIndex),currentIndex,new TrackLineMoveListener());
        firstShow = false;

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


    class  TrackLineMoveListener implements SmoothMoveMarker.MoveListener{
        private boolean hasExecuted = false;
        private double pre = -100;

        @Override
        public void move(double v) {
            CFLog.e(AMapImpl.TAG,"remain distance ="+v );
            if(!hasExecuted && v == 0 && currentIndex < mDataSource.size()
                    && Math.abs(pre - v) < 10 ){
                CFLog.e(AMapImpl.TAG,"a line finished, prepare to draw new line");
                hasExecuted = true;
                currentIndex ++;
                AMapImpl.getInstance().drawTrackLine(mDataSource.get(currentIndex),currentIndex,new TrackLineMoveListener());
            }
            pre = v;
        }
    }
}
