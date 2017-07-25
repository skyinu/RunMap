package com.stdnull.runmap.modules.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 权限请求类
 * Created by chen on 2017/1/23.
 */

public class PermissionRequest{
    private PermissionCallBack mCallBack;
    private String [] mRequestPermission;
    private Activity activity;

    public PermissionRequest(Activity activity, String []permissions,PermissionCallBack mCallBack){
        this.activity = activity;
        this.mCallBack = mCallBack;
        this.mRequestPermission = permissions;
    }

    boolean startRequest(){
        boolean flag = false;
        for(int i = 0; i< mRequestPermission.length; i++){
            if(PackageManager.PERMISSION_DENIED == activity.checkCallingOrSelfPermission(mRequestPermission[i])){
                flag = true;
                break;
            }
        }
        if(!flag){
            mCallBack.onAllPermissionGranted();
            return true;
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(mRequestPermission, PermissionManager.PERMISSION_CODE);
        }
        return false;
    }

    public PermissionCallBack getCallBack() {
        return mCallBack;
    }
}
