package com.stdnull.runmap.managers;

import com.stdnull.runmap.activity.BaseActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * 系统页面管理类
 * Created by chen on 2017/1/27.
 */

public class ActivityContextManager {
    private List<BaseActivity> mActiveActivity;
    private static ActivityContextManager mInstance;

    private static final  Object INSTANCE_LOCK = new Object();
    private ActivityContextManager(){
        mActiveActivity = new LinkedList<>();
    }

    public static ActivityContextManager getInstance(){
        if(mInstance == null){
            synchronized (INSTANCE_LOCK){
                if(mInstance == null){
                    mInstance = new ActivityContextManager();
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

    public BaseActivity getLatestActivity(){
        if(mActiveActivity.isEmpty()){
            return null;
        }
        else{
            return mActiveActivity.get(mActiveActivity.size()-1);
        }
    }
}
