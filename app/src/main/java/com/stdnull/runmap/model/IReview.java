package com.stdnull.runmap.model;

import com.amap.api.maps.model.LatLng;

import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/6/3.
 */

public interface IReview {
    Map<Integer, List<TrackPoint>> getPreviewData();

    void setPreviewData(Map<Integer, List<TrackPoint>> previewData);

     Map<Integer, List<TrackPoint>> getCurrentShowData();

    void setCurrentShowData(Map<Integer, List<TrackPoint>> currentShowData);

    Map<Integer, List<TrackPoint>> getNextData();

    void setNextData(Map<Integer, List<TrackPoint>> nextData);

    List<String> getDateList();

    void setDateList(List<String> dateList);

    void setCurrentPosition(int index);

    int getCurrentPosition();

    List<List<LatLng>> formatTrackPoints();
}
