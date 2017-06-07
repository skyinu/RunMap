package com.stdnull.runmap.common;

/**
 * Created by chen on 2017/6/7.
 */

public class CFCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final CFCrashHandler mInstance = new CFCrashHandler();

    public static CFCrashHandler getInstance(){
        return mInstance;
    }

    public void registCrashHandler(){
        Thread.setDefaultUncaughtExceptionHandler(mInstance);
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        CFLog.e("CFCrashHandler", "Thread is " + t.getName() + "error is " + e.getMessage());
    }
}
