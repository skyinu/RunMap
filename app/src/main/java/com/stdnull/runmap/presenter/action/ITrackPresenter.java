package com.stdnull.runmap.presenter.action;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

/**
 * Created by chen on 2017/6/2.
 */

public interface ITrackPresenter {
    void initAmap(Activity activity);
    void registerWXShareAPI(Context context);
    int getMapType();
    void changeMapType();
    void showMapUiLayout(Context context, RelativeLayout mapUI, Fragment mapFrag);
    void showDataUiLayout(Context context, RelativeLayout mapUI, Fragment mapFrag);
    void onBackPressed(Context context);
    void scaleCurrentCamera();
    void onRestoreInstanceState(Bundle bundle);
    void onSaveInstanceState(Bundle bundle);
    void share(int shareId);
    void startForeInfoService(Context context);
    void onActicityCreated();
    void onActivityDestoryed();
}
