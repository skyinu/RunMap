package com.stdnull.runmap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * 坐标数据的数据结构
 * Created by chen on 2017/1/30.
 */

public class TrackPoint implements Parcelable{
    private LatLng location;

    private String buildName;

    private long timeStamp;


    public TrackPoint(LatLng location, String buildName, long timeStamp) {
        this.location = location;
        this.buildName = buildName;
        this.timeStamp = timeStamp;
    }

    public TrackPoint(LatLng location, long timeStamp) {
        this.location = location;
        this.timeStamp = timeStamp;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public double getLatitude() {
        return location.latitude;
    }

    public double getLongitude() {
        return location.longitude;
    }

    public String getBuildName() {
        return buildName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public LatLng getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected TrackPoint(Parcel in) {
        location = in.readParcelable(LatLng.class.getClassLoader());
        buildName = in.readString();
        timeStamp = in.readLong();
    }

    public static final Creator<TrackPoint> CREATOR = new Creator<TrackPoint>() {
        @Override
        public TrackPoint createFromParcel(Parcel in) {
            return new TrackPoint(in);
        }

        @Override
        public TrackPoint[] newArray(int size) {
            return new TrackPoint[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
        dest.writeString(buildName);
        dest.writeLong(timeStamp);
    }
}
