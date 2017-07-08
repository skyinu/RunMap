package com.stdnull.runmap.model;

import android.util.SparseArray;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/6/3.
 */

public class ReviewModel implements IReview {
    /**
     * data list to show in map
     */
    private SparseArray<List<TrackPoint>> mPreviewData;//前一天数据
    private SparseArray<List<TrackPoint>> mCurrentShowData;//当前查看数据
    private SparseArray<List<TrackPoint>> mNextData;//下一天数据
    /**
     * date list
     */
    private List<String> mDateList;
    /**
     * 当前查看日期数据的索引
     */
    private int mCurrentPosition;


    @Override
    public SparseArray<List<TrackPoint>> getPreviewData() {
        return mPreviewData;
    }

    @Override
    public void setPreviewData(SparseArray<List<TrackPoint>> previewData) {
        this.mPreviewData = previewData;
    }

    @Override
    public SparseArray<List<TrackPoint>> getCurrentShowData() {
        return mCurrentShowData;
    }

    @Override
    public void setCurrentShowData(SparseArray<List<TrackPoint>> currentShowData) {
        this.mCurrentShowData = currentShowData;
    }

    @Override
    public SparseArray<List<TrackPoint>> getNextData() {
        return mNextData;
    }

    @Override
    public void setNextData(SparseArray<List<TrackPoint>> nextData) {
        this.mNextData = nextData;
    }

    @Override
    public List<String> getDateList() {
        return mDateList;
    }

    @Override
    public void setDateList(List<String> dateList) {
        this.mDateList = dateList;
    }

    @Override
    public void setCurrentPosition(int index) {
        this.mCurrentPosition = index;
    }

    @Override
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    @Override
    public List<List<LatLng>> formatTrackPoints() {
        int count = mCurrentShowData.size();
        List<List<LatLng>> groupData = new ArrayList<>();
        for(int i=0;i<count;i++) {
            List<LatLng> points = new ArrayList<>();
            List<TrackPoint> trackPointList = mCurrentShowData.get(mCurrentShowData.keyAt(i));
            for (int j = 0; j < trackPointList.size(); j++) {
                points.add(trackPointList.get(j).getLocation());
            }
            groupData.add(points);
        }
        return groupData;
    }
}
