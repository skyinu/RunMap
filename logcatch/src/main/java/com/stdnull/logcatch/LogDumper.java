package com.stdnull.logcatch;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chen on 2017/7/15.
 */

public class LogDumper {
    static InputStream dumpLog(){
        try {
            Process process = Runtime.getRuntime().exec("logcat *:D -d");
            Runtime.getRuntime().exec("logcat -c");
            return process.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
