package com.stdnull.baselib.logcatch;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chen on 2017/7/15.
 */

public class WirelessLog {
    private ScheduledExecutorService mLogCatchExecutor = Executors.newScheduledThreadPool(2);
    private volatile static WirelessLog mInstance;
    private static final Object mInstanceLock = new Object();
    private WirelessLog(){
    }

    public static WirelessLog getInstance(){
        if(mInstance == null) {
            synchronized (mInstanceLock) {
                if(mInstance == null){
                    mInstance = new WirelessLog();
                }
            }
        }
        return mInstance;
    }

    public void catchLogs(String host){
        mLogCatchExecutor.submit(new DumpTask(host));
    }

    public void catchLogsPeriod(final String host, final long period, final TimeUnit time){
        mLogCatchExecutor.schedule(new DumpTask(host){
            @Override
            public void afterSendLog() {
                super.afterSendLog();
                catchLogsPeriod(host, period, time);
            }
        }, period, time);
    }
}
