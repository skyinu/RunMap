package com.stdnull.runmap.managers;

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
 * Created by chen on 2017/1/19.
 */

public class AppManager implements Application.ActivityLifecycleCallbacks{
    private static AppManager mInstance = new AppManager();

    private boolean isFirstForeground = true;
    private int mActiveActivity = 0;
    private List<WeakReference<AppStateListener>> mAppStateListeners;

    public AppManager(){
        mAppStateListeners = new ArrayList<>();
    }

    public static AppManager getInstance(){
        return mInstance;
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(mActiveActivity++ == 0){
            if(!isFirstForeground) {
                notifyForeground(activity);
            }
            isFirstForeground = false;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActiveActivity--;
        if(mActiveActivity == 0){
            notifyBackground(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

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

    public void registerListener(@NonNull AppStateListener listener){
        WeakReference<AppStateListener> item = new WeakReference<>(listener);
        mAppStateListeners.add(item);
    }

    public void unRegisterListener(@NonNull AppStateListener listener){
        for(int i=0;i<mAppStateListeners.size();i++){
            AppStateListener item = mAppStateListeners.get(i).get();
            if(listener.equals(item)){
                mAppStateListeners.remove(i);
                return;
            }
        }
    }


    public interface AppStateListener{
        void onForeground(Context context);
        void onBackground(Context context);
    }
}
