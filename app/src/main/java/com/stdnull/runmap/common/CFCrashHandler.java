package com.stdnull.runmap.common;

/**
 * Created by chen on 2017/6/7.
 */

public class CFCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final CFCrashHandler mInstance = new CFCrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static CFCrashHandler getInstance(){
        return mInstance;
    }

    public void registCrashHandler(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mInstance);
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        mDefaultHandler.uncaughtException(t, e);
        CFLog.e("CFCrashHandler", "Thread is " + t.getName() + "error is " + e.getMessage());
    }
}
