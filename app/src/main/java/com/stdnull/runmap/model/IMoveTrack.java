package com.stdnull.runmap.model;

import android.os.Bundle;

import java.util.List;


/**
 * Created by chen on 2017/6/3.
 */

public interface IMoveTrack {
    void onRestoreInstanceState(Bundle bundle);
    void onSaveInstanceState(Bundle bundle);
    long onNewLocation(TrackPoint trackPoint);
    long updateDuration(int addedTime);
    List<TrackPoint> getHistoryCoordiates();
    void saveModelToDatabase(boolean isEnd);
}
