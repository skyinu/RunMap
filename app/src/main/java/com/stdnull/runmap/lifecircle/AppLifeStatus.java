package com.stdnull.runmap.lifecircle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.stdnull.runmap.common.CFLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用状态管理
 * Created by chen on 2017/1/19.
 */

class AppLifeStatus implements Application.ActivityLifecycleCallbacks{
    //to indicate whether app is first launched
    private boolean isFirstForeground = true;
    //the active activity number,in this case, the value may always one
    private int mActiveActivity = 0;
    private Activity mCurrentActivity;
    //app state listener lists
    private List<WeakReference<AppStateListener>> mAppStateListeners;

    public AppLifeStatus(){
        mAppStateListeners = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(mActiveActivity ++ == 0){
            if(!isFirstForeground) {
                notifyForeground(activity);

            }
            isFirstForeground = false;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mCurrentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mCurrentActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if(--mActiveActivity == 0){
            notifyBackground(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    private void notifyForeground(Context context){
        CFLog.e(this.getClass().getName(),"onForeground");
        for(int i=0;i<mAppStateListeners.size();i++){
            AppStateListener listener = mAppStateListeners.get(i).get();
            if(listener != null){
                listener.onForeground(context);
            }
        }

    }

    private void notifyBackground(Context context){
        CFLog.e(this.getClass().getName(),"onBackground");
        for(int i=0;i<mAppStateListeners.size();i++){
            AppStateListener listener = mAppStateListeners.get(i).get();
            if(listener != null){
                listener.onBackground(context);
            }
        }
    }

    protected void registerListener(@NonNull AppStateListener listener){
        WeakReference<AppStateListener> item = new WeakReference<>(listener);
        mAppStateListeners.add(item);
    }

    protected void unRegisterListener(@NonNull AppStateListener listener){
        for(int i=0;i<mAppStateListeners.size();i++){
            AppStateListener item = mAppStateListeners.get(i).get();
            if(listener.equals(item)){
                mAppStateListeners.remove(i);
                return;
            }
        }
    }


}
