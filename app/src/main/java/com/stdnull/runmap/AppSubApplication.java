package com.stdnull.runmap;

import com.squareup.leakcanary.LeakCanary;
import com.stdnull.baselib.GlobalApplication;
import com.stdnull.runmap.common.AppConfig;

/**
 * Created by chen on 2017/9/29.
 */

public class AppSubApplication extends GlobalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        AppConfig.runCheck();
    }
}
