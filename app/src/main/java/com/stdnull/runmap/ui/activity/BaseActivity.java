package com.stdnull.runmap.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.lifecircle.LifeCycleMonitor;
import com.stdnull.runmap.modules.permission.PermissionManager;

/**
 * 所有Activity的基类
 * Created by chen on 2016/12/26.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CFLog.i(this.getClass().getName(),"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        CFLog.i(this.getClass().getName(),"onStart");
        LifeCycleMonitor.getInstance().registerActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CFLog.i(this.getClass().getName(),"onRestart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        CFLog.i(this.getClass().getName(),"onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        CFLog.i(this.getClass().getName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CFLog.i(this.getClass().getName(),"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        CFLog.i(this.getClass().getName(),"onStop");
        LifeCycleMonitor.getInstance().unRegisterActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CFLog.i(this.getClass().getName(),"onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CFLog.i(this.getClass().getName(),"onDestroy");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance().handlePermissionResult(requestCode,permissions,grantResults);
    }

    public AlertDialog showSettingDialog(final String setting, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.string_cancel,null);
        builder.setPositiveButton(R.string.string_goto_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =  new Intent(setting);
                startActivity(intent);
            }
        });
        return builder.create();
    }
}
