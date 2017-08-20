package com.stdnull.baselib.logcatch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chen on 2017/7/15.
 */

public class DumpTask implements Runnable {
    private String mHost;
    public DumpTask(String host){
        this.mHost = host;
    }
    @Override
    public void run() {
        InputStream inputStream = LogDumper.dumpLog();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        beforeSendLog();
        LogSender.sendLogs(mHost, br);
        afterSendLog();

    }

    public void beforeSendLog(){

    }

    public void afterSendLog(){

    }
}
