package com.stdnull.runmap.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.managers.PermissionManager;
import com.stdnull.runmap.permission.PermissionCallBack;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionCallBack permissionCallBack =new PermissionCallBack() {
            @Override
            public void onAllPermissionGranted() {
                CFLog.e("tag","Hello Granted");
            }

            @Override
            public void onDenied() {

            }
        };
        PermissionManager.getInstance().requestPermission(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},permissionCallBack);
    }
}
