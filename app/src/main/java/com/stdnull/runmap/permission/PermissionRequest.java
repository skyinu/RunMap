package com.stdnull.runmap.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.stdnull.runmap.activity.BaseActivity;
import com.stdnull.runmap.managers.PermissionManager;

/**
 * 权限请求类
 * Created by chen on 2017/1/23.
 */

public class PermissionRequest implements PermissionCallBack{
    private PermissionCallBack mCallBack;
    private String [] mRequestPermission;
    private BaseActivity activity;

    public PermissionRequest(BaseActivity activity, String []permissions,PermissionCallBack mCallBack){
        this.activity = activity;
        this.mCallBack = mCallBack;
        this.mRequestPermission = permissions;
    }

    public void startRequest(){
        boolean flag = false;
        for(int i = 0; i< mRequestPermission.length; i++){
            if(PackageManager.PERMISSION_DENIED == activity.checkCallingOrSelfPermission(mRequestPermission[i])){
                flag = true;
                break;
            }
        }
        if(!flag){
            mCallBack.onAllPermissionGranted();
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(mRequestPermission, PermissionManager.PERMISSION_CODE);
        }
    }

    @Override
    public void onAllPermissionGranted() {
        mCallBack.onAllPermissionGranted();
    }

    @Override
    public void onDenied() {
        mCallBack.onDenied();
    }
}
