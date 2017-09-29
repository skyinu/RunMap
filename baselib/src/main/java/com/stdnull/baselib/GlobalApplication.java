package com.stdnull.baselib;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.stdnull.baselib.common.CFCrashHandler;
import com.stdnull.baselib.common.CFLog;
import com.stdnull.baselib.lifecircle.LifeCycleMonitor;
import com.stdnull.baselib.utils.SystemUtils;

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
        String processName = SystemUtils.getProcessName(this);
        if(!TextUtils.isEmpty(processName)&& getPackageName().equals(processName)) {
            mAppContext = this;
            init();
            CFLog.initMonitor();
        }
    }

    public static Context getAppContext(){
        return mAppContext;
    }

    private void init(){
        registerActivityLifecycleCallbacks(LifeCycleMonitor.getInstance().getLifeCycleCallBack());
        CFCrashHandler.getInstance().registCrashHandler();
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
