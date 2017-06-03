package com.stdnull.runmap.common;

import android.util.Log;

import com.stdnull.runmap.BuildConfig;

/**
 * 日志打印工具类
 * Created by chen on 2017/1/19.
 */

public class CFLog {
    public static final boolean DEBUG = BuildConfig.DEBUG;
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
