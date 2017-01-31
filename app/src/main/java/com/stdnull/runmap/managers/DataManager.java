package com.stdnull.runmap.managers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.bean.LocationBean;
import com.stdnull.runmap.bean.LocationDataBase;
import com.stdnull.runmap.bean.TrackPoint;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.map.AmLocationManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/1/27.
 */

public class DataManager {
    private static DataManager mInstance;
    private static final Object INSTANCE_LOCK = new Object();

    //管理经纬度数据对象
    private LocationBean mLocationBean;
    //数据库实例
    private LocationDataBase mLocationDataBase;

    private DataManager(){
        mLocationBean = new LocationBean();
        mLocationDataBase = new LocationDataBase(GlobalApplication.getAppContext(),
                RMConfiguration.DATABASE_NAME, null,1);
    }

    public static DataManager getInstance(){
        if(mInstance == null){
            synchronized (INSTANCE_LOCK){
                if(mInstance == null){
                    mInstance = new DataManager();
                }
            }
        }
        return mInstance;
    }

    public void addTrackPoint(TrackPoint trackPoint){
        mLocationBean.addPointDatas(trackPoint);
    }

    public List<TrackPoint> getTrackPoints(){
        return mLocationBean.getPointDatas();
    }

    public void clearDataInMemory(){
        mLocationBean.getLatDatas().clear();
    }

    public void cacheDataToDatabase(){
        CFAsyncTask task = new CFAsyncTask<Void>() {
            @Override
            public Void onTaskExecuted(Object... params) {
                LocationDataBase locationDataBase = (LocationDataBase) params[0];
                SQLiteDatabase db = locationDataBase.getWritableDatabase();
                Calendar calendar = Calendar.getInstance();
                int date = 10000*calendar.get(Calendar.YEAR)+100*(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DATE);
                int count = DataManager.this.queryRecordCountToday(date) + 1;
                LocationBean locationBean = (LocationBean) params[1];
                List<TrackPoint> trackPointList = locationBean.getPointDatas();
                for(int i=0;i<trackPointList.size();i++){
                    TrackPoint point = trackPointList.get(i);
                    ContentValues values = new ContentValues();
                    values.put(LocationDataBase.FILED_LATITUDE,point.getLatitude());
                    values.put(LocationDataBase.FILED_LONGITUDE,point.getLongitude());
                    values.put(LocationDataBase.FILED_TIME_STAMP,point.getTimeStamp());
                    values.put(LocationDataBase.FILED_BUILD_NAME,point.getBuildName());
                    values.put(LocationDataBase.FILED_TIME_DAY,date);
                    values.put(LocationDataBase.FILED_RECORD_COUNT,count);
                    db.insert(LocationDataBase.TABLE_LOCATION,null,values);
                }
                //每条数据增加一个点，用于避免最后一个点时间计算不准的情况
                TrackPoint point = trackPointList.get(trackPointList.size()-1);
                ContentValues values = new ContentValues();
                values.put(LocationDataBase.FILED_LATITUDE,point.getLatitude());
                values.put(LocationDataBase.FILED_LONGITUDE,point.getLongitude());
                values.put(LocationDataBase.FILED_TIME_STAMP,point.getTimeStamp());
                values.put(LocationDataBase.FILED_BUILD_NAME,point.getBuildName());
                values.put(LocationDataBase.FILED_TIME_DAY,date);
                values.put(LocationDataBase.FILED_RECORD_COUNT,count);
                db.insert(LocationDataBase.TABLE_LOCATION,null,values);
                return null;
            }

            @Override
            public void onTaskFinished(Void result) {

            }
        };
        TaskHanler.getInstance().sendTask(task,mLocationDataBase,mLocationBean);
    }

    /**
     * 按时间序获取最近30条数据
     * @return
     */
    public List<String> queryDataTime(){
        List<String> mDataTime = new ArrayList<>();
        String query_sql = "select  " + LocationDataBase.FILED_TIME_DAY + " from " + LocationDataBase.TABLE_LOCATION
                + " order by " + LocationDataBase.FILED_TIME_DAY + " desc ";
        SQLiteDatabase db = mLocationDataBase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_sql, null);
        try {
            while (cursor.moveToNext()){
                int index= cursor.getColumnIndex(LocationDataBase.FILED_TIME_DAY);
                String date = cursor.getString(index);
                if(mDataTime.size() > RMConfiguration.MAX_SUPPORT_ITEMS){
                    break;
                }
                if(!mDataTime.contains(date)) {
                    mDataTime.add(date);
                }
                CFLog.e(AmLocationManager.TAG,date);
            }
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return mDataTime;

    }

    public int queryRecordCountToday(int dayTime){
        String query_sql = "select  "+LocationDataBase.FILED_RECORD_COUNT + " from " + LocationDataBase.TABLE_LOCATION
                + " where " + LocationDataBase.FILED_TIME_DAY + " = " + dayTime
                + " order by " + LocationDataBase.FILED_RECORD_COUNT + " desc ";
        SQLiteDatabase db = mLocationDataBase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_sql, null);
        int result = -1;
        try {
            while (cursor.moveToNext()){
                int index= cursor.getColumnIndex(LocationDataBase.FILED_RECORD_COUNT);
                result =  cursor.getInt(index);
                CFLog.e(AmLocationManager.TAG,"current count = "+result);
            }
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return result;

    }

    /**
     * 按日期检索位置信息，以升序形式返回
     * @param dayTime
     * @return
     */
    public Map<Integer,List<TrackPoint>> readTrackPointFormDataBase(String dayTime) {
        Map<Integer, List<TrackPoint>> groupResult = new HashMap<>();
        String query_sql = "select * from " + LocationDataBase.TABLE_LOCATION
                + " group by " + LocationDataBase.FILED_RECORD_COUNT
                + " order by " + LocationDataBase.FILED_TIME_STAMP + " asc ";
        SQLiteDatabase db = mLocationDataBase.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_sql, null);
        try {
            while (cursor.moveToNext()) {
                int latitudeIndex = cursor.getColumnIndex(LocationDataBase.FILED_LATITUDE);
                int longitudeIndex = cursor.getColumnIndex(LocationDataBase.FILED_LONGITUDE);
                int timeStampIndex = cursor.getColumnIndex(LocationDataBase.FILED_TIME_STAMP);
                int buildingIndex = cursor.getColumnIndex(LocationDataBase.FILED_BUILD_NAME);
                int countIndex = cursor.getColumnIndex(LocationDataBase.FILED_RECORD_COUNT);
                double latitude = cursor.getDouble(latitudeIndex);
                double longitude = cursor.getDouble(longitudeIndex);
                long timeStamp = Long.valueOf(cursor.getString(timeStampIndex));
                String building = cursor.getString(buildingIndex);
                int count = cursor.getInt(countIndex);
                List<TrackPoint> pointList = groupResult.get(count);
                if (pointList == null) {
                    pointList = new ArrayList<>();
                    groupResult.put(count,pointList);
                }
                TrackPoint point = new TrackPoint(new LatLng(latitude,longitude), building, timeStamp);
                pointList.add(point);
            }
        }
        finally{
            if(cursor!=null){
                cursor.close();
            }
        }
        return groupResult;
    }
}
