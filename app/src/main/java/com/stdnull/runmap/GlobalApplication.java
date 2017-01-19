package com.stdnull.runmap;

import android.app.Application;
import android.content.Context;

/**
 * Created by chen on 2017/1/19.
 */

public class GlobalApplication extends Application {
    private Context mAppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }
}
