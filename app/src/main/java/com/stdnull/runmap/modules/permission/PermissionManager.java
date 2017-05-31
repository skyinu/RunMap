package com.stdnull.runmap.modules.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 权限管理类
 * Created by chen on 2017/1/19.
 */

public class PermissionManager {
    private static PermissionManager mInstance = new PermissionManager();

    public static final int PERMISSION_CODE = 0xf0;

    private Queue<PermissionRequest> mCurrentRequest = new LinkedList<>();
    public static PermissionManager getInstance(){
        return mInstance;
    }

    public void requestPermission(Activity activity, String[]permissons, PermissionCallBack callBack){
        PermissionRequest request = new PermissionRequest(activity,permissons,callBack);
        mCurrentRequest.add(request);
        request.startRequest();
    }

    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        PermissionRequest request = mCurrentRequest.poll();
        PermissionCallBack callBack = request.getCallBack();
        if(requestCode != PERMISSION_CODE || callBack == null){
            return;
        }
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                callBack.onDenied();
                return;
            }
        }
         callBack.onAllPermissionGranted();
    }
}
