package com.stdnull.runmap;

import android.app.Application;
import android.content.Context;

import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.lifecircle.LifeCycleMonitor;

/**
 * app进程类
 * Created by chen on 2017/1/19.
 */

public class GlobalApplication extends Application {
    private static Context mAppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        CFLog.e(this.getClass().getName(),"onCreate");
        mAppContext = this;
        init();
    }

    public static Context getAppContext(){
        return mAppContext;
    }

    private void init(){
        registerActivityLifecycleCallbacks(LifeCycleMonitor.getInstance().getLifeCycleCallBack());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        CFLog.e(this.getClass().getName(),"onLowMemory");
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        CFLog.e(this.getClass().getName(),"onTrimMemory level = " + level);

    }
}
