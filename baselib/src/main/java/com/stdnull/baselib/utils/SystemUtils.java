package com.stdnull.baselib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.stdnull.baselib.GlobalApplication;
import com.stdnull.baselib.common.RMConfiguration;

/**
 * 系统服务查询工具类
 * Created by chen on 2017/1/20.
 */

public final class SystemUtils {
    public static final int STATE_INSTALL = 0x0;
    public static final int STATE_UPDATE = 0x1;
    public static final int STATE_NORMAL = 0x2;

    public static int getApplicationState(Context context) throws PackageManager.NameNotFoundException {
        SharedPreferences sp = context.getSharedPreferences(RMConfiguration.FILE_CONFIG,Context.MODE_PRIVATE);
        String cacheVersion = sp.getString(RMConfiguration.KEY_VERSION, "");
        PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
        String curVersion = info.versionName + info.versionCode;
        if(StringUtils.isEmpty(cacheVersion)){
            return STATE_INSTALL;
        }
        if(!curVersion.equals(cacheVersion)){
            return STATE_UPDATE;
        }
        return STATE_NORMAL;
    }

    public static boolean isGpsEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkEnable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();//获取进程pid
        String processName = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);//获取系统的ActivityManager服务
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                return processName;
            }
        }
        return processName;
    }

    public static String getDbPath(){
        StringBuilder dbPath = new StringBuilder("/data/data/");
        dbPath.append(GlobalApplication.getAppContext().getPackageName());
        dbPath.append("/databases");
        dbPath.append("/" + RMConfiguration.DATABASE_NAME);
        return dbPath.toString();
    }
}
