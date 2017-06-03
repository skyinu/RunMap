package com.stdnull.runmap.model;

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
    private Map<Integer,List<TrackPoint>> mPreviewData;//前一天数据
    private Map<Integer,List<TrackPoint>> mCurrentShowData;//当前查看数据
    private Map<Integer,List<TrackPoint>> mNextData;//下一天数据
    /**
     * date list
     */
    private List<String> mDateList;
    /**
     * 当前查看日期数据的索引
     */
    private int mCurrentPosition;


    @Override
    public Map<Integer, List<TrackPoint>> getPreviewData() {
        return mPreviewData;
    }

    @Override
    public void setPreviewData(Map<Integer, List<TrackPoint>> previewData) {
        this.mPreviewData = previewData;
    }

    @Override
    public Map<Integer, List<TrackPoint>> getCurrentShowData() {
        return mCurrentShowData;
    }

    @Override
    public void setCurrentShowData(Map<Integer, List<TrackPoint>> currentShowData) {
        this.mCurrentShowData = currentShowData;
    }

    @Override
    public Map<Integer, List<TrackPoint>> getNextData() {
        return mNextData;
    }

    @Override
    public void setNextData(Map<Integer, List<TrackPoint>> nextData) {
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
        int count = mCurrentShowData.keySet().size();
        List<List<LatLng>> groupData = new ArrayList<>();
        for(int i=0;i<count;i++) {
            List<LatLng> points = new ArrayList<>();
            List<TrackPoint> trackPointList = mCurrentShowData.get(i);
            for (int j = 0; j < trackPointList.size(); j++) {
                points.add(trackPointList.get(j).getLocation());
            }
            groupData.add(points);
        }
        return groupData;
    }
}
