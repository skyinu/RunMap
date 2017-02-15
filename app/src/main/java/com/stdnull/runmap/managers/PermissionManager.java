package com.stdnull.runmap.managers;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.stdnull.runmap.activity.BaseActivity;
import com.stdnull.runmap.permission.PermissionCallBack;
import com.stdnull.runmap.permission.PermissionRequest;

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

    public void requestPermission(BaseActivity activity, String[]permissons, PermissionCallBack callBack){
        PermissionRequest request = new PermissionRequest(activity,permissons,callBack);
        mCurrentRequest.add(request);
        request.startRequest();
    }

    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode != PERMISSION_CODE){
            return;
        }
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                PermissionRequest request = mCurrentRequest.poll();
                request.onDenied();
                return;
            }
        }
        PermissionRequest request = mCurrentRequest.poll();
        request.onAllPermissionGranted();

    }
}
