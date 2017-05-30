package com.stdnull.runmap.modules.permission;

/**
 * 权限请求结果的回调
 * Created by chen on 2017/1/23.
 */

public interface PermissionCallBack {
    void onAllPermissionGranted();
    void onDenied();
}
