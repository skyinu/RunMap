package com.stdnull.runmap.common;

import android.util.Log;

import com.stdnull.runmap.BuildConfig;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 日志打印工具类
 * Created by chen on 2017/1/19.
 */

public class CFLog {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static Object mWirelessLog;

    public static void initMonitor(){
        if(DEBUG){
            try {
                Class wirelessLogClass = Class.forName("com.stdnull.logcatch.WirelessLog");
                Method instanceMethod = wirelessLogClass.getDeclaredMethod("getInstance");
                mWirelessLog = instanceMethod.invoke(wirelessLogClass);
                Method catchLogsPeriod = wirelessLogClass.getDeclaredMethod("catchLogsPeriod",String.class, int.class,  TimeUnit.class);
                catchLogsPeriod.invoke(mWirelessLog, "192.168.18.7", 10, TimeUnit.SECONDS);
            } catch (Exception e) {
            }
        }
    }
    public static void e(String tag, String msg){
        if(DEBUG){
            Log.e(tag,msg);
        }
    }
    public static void d(String tag, String msg){
        if(DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if(DEBUG){
            Log.i(tag,msg);
        }
    }

}
