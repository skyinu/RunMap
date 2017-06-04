package com.stdnull.runmap.model;

import android.os.Bundle;
import android.os.Parcelable;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.managers.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/6/3.
 */

public class MoveTrackModel implements IMoveTrack {
    private long mDurationTime = 0;
    private long mDurationDistance = 0;
    private List<TrackPoint> mCoordinateLists = new ArrayList<>();

    private static final String KEY_DURATION_TIME = "KEY_DURATION_TIME";
    private static final String KEY_DURATION_DISTANCE = "KEY_DURATION_DISTANCE";
    private static final String KEY_TRACK_POINT = "KEY_TRACK_POINT";

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        mDurationDistance = bundle.getLong(KEY_DURATION_DISTANCE);
        mDurationTime = bundle.getLong(KEY_DURATION_TIME);
        mCoordinateLists = bundle.getParcelableArrayList(KEY_TRACK_POINT);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(KEY_TRACK_POINT, (ArrayList<? extends Parcelable>) mCoordinateLists);
        bundle.putLong(KEY_DURATION_TIME, mDurationTime);
        bundle.putLong(KEY_DURATION_DISTANCE, mDurationDistance);
    }

    @Override
    public long onNewLocation(TrackPoint trackPoint) {
        LatLng cur = new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude());
        if(mCoordinateLists.isEmpty()){
            mCoordinateLists.add(trackPoint);
            return 0;
        }
        LatLng pre = mCoordinateLists.get(mCoordinateLists.size() - 1).getLocation();
        mCoordinateLists.add(trackPoint);
        mDurationDistance += AMapUtils.calculateLineDistance(pre, cur);
        return mDurationDistance;
    }

    @Override
    public long updateDuration(int addedTime) {
        mDurationTime += addedTime;
        return mDurationTime;
    }

    @Override
    public List<TrackPoint> getHistoryCoordiates() {
        return mCoordinateLists;
    }

    @Override
    public void saveModelToDatabase(boolean isEnd) {
        DataManager.getInstance().saveDataAndClearMemory(mCoordinateLists,mDurationDistance, isEnd);
    }
}
