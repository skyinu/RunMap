package com.stdnull.runmap.permission;

/**
 * Created by chen on 2017/1/23.
 */

public interface PermissionCallBack {
    void onAllPermissionGranted();
    void onDenied();
}
