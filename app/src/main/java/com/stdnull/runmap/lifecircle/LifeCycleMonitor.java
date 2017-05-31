package com.stdnull.runmap.lifecircle;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.stdnull.runmap.ui.activity.BaseActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * 系统页面管理类
 * Created by chen on 2017/1/27.
 */

public class LifeCycleMonitor {
    //singleton lock object
    private static final  Object INSTANCE_LOCK = new Object();
    private static LifeCycleMonitor mInstance;
    //active activities
    private List<BaseActivity> mActiveActivity;
    private AppLifeStatus mLifeStatus;


    private LifeCycleMonitor(){
        mActiveActivity = new LinkedList<>();
        mLifeStatus = new AppLifeStatus();
    }

    public static LifeCycleMonitor getInstance(){
        if(mInstance == null){
            synchronized (INSTANCE_LOCK){
                if(mInstance == null){
                    mInstance = new LifeCycleMonitor();
                }
            }
        }
        return mInstance;
    }

    public void registerActivity(BaseActivity activity){
        if(mActiveActivity.contains(activity)){
            mActiveActivity.remove(activity);
        }
        mActiveActivity.add(activity);
    }

    public void unRegisterActivity(BaseActivity activity){
        if(mActiveActivity.contains(activity)){
            mActiveActivity.remove(activity);
        }
    }

    public Activity getLatestActivity(){
        if(mActiveActivity.isEmpty()){
            return mLifeStatus.getCurrentActivity();
        }
        else{
            return mActiveActivity.get(mActiveActivity.size()-1);
        }
    }

    public Application.ActivityLifecycleCallbacks getLifeCycleCallBack(){
        return mLifeStatus;
    }
    public void registerListener(@NonNull AppStateListener listener){
        mLifeStatus.registerListener(listener);
    }

    public void unRegisterListener(@NonNull AppStateListener listener){
        mLifeStatus.unRegisterListener(listener);
    }
}
