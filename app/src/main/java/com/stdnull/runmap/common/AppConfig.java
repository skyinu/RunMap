package com.stdnull.runmap.common;

/**
 * Created by chen on 2017/7/16.
 */

public class AppConfig {
    static {
        System.loadLibrary("config");
    }
    public native static String getWeixinAppId();
    public native static String getDbBackupBaseUrl();
    public native static boolean runCheck();
}
