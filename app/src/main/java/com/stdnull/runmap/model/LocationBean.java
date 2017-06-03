package com.stdnull.runmap.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储数据点的类
 * Created by chen on 2017/1/27.
 */

public class LocationBean {

    private List<TrackPoint> mPointDatas;

    public LocationBean(List<TrackPoint> pointDatas) {
        this.mPointDatas = pointDatas;
    }

    public LocationBean() {
        mPointDatas = new ArrayList<>();
    }

    public List<TrackPoint> getPointDatas() {
        return mPointDatas;
    }

    public void addPointDatas(TrackPoint points) {
        mPointDatas.add(points);
    }

}
