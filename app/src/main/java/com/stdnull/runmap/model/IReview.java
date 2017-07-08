package com.stdnull.runmap.model;

import android.util.SparseArray;

import com.amap.api.maps.model.LatLng;

import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/6/3.
 */

public interface IReview {
    SparseArray<List<TrackPoint>> getPreviewData();

    void setPreviewData(SparseArray<List<TrackPoint>> previewData);

     SparseArray<List<TrackPoint>> getCurrentShowData();

    void setCurrentShowData(SparseArray<List<TrackPoint>> currentShowData);

    SparseArray<List<TrackPoint>> getNextData();

    void setNextData(SparseArray<List<TrackPoint>> nextData);

    List<String> getDateList();

    void setDateList(List<String> dateList);

    void setCurrentPosition(int index);

    int getCurrentPosition();

    List<List<LatLng>> formatTrackPoints();
}
