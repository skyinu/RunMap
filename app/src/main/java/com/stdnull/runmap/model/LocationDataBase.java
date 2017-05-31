package com.stdnull.runmap.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * Created by chen on 2017/1/28.
 */

public class LocationDataBase extends SQLiteOpenHelper {
    public static final String TABLE_LOCATION = "LATLNG";
    public static final String FILED_ID = "id";
    public static final String FILED_TIME_STAMP = "data_time";
    public static final String FILED_RECORD_COUNT = "count";
    public static final String FILED_LATITUDE = "latitude";
    public static final String FILED_LONGITUDE = "longitude";
    public static final String FILED_BUILD_NAME = "build_name";
    public static final String FILED_TIME_DAY = "time_day";

    public static final String SQL_LOCATION_CREATE ="CREATE TABLE IF NOT EXISTS "
            + TABLE_LOCATION + "("
            + FILED_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FILED_LATITUDE + " DOUBLE, "
            + FILED_LONGITUDE + " DOUBLE, "
            + FILED_BUILD_NAME + " TEXT, "
            + FILED_RECORD_COUNT + " INTEGER, "
            + FILED_TIME_STAMP + " CHAR(65), "
            + FILED_TIME_DAY + " INTEGER)";

    public LocationDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_LOCATION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
