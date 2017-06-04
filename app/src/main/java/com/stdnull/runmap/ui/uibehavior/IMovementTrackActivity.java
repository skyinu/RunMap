package com.stdnull.runmap.ui.uibehavior;


import android.support.v7.app.AlertDialog;

/**
 * Created by chen on 2017/6/2.
 */

public interface IMovementTrackActivity {
    /**
     * @param distanceGap 增加的运动距离
     */
    void updateDistance(String distanceGap);

    /**
     * @param time 增长的运动时间
     */
    void updateTime(String time);

    /**
     * @param gpsPower GPS信号强度
     */
    void updateGpsPower(int gpsPower);

    /**
     * 创建设置Dialog
     * @return
     */
    AlertDialog showGpsSettingDialog(String action, int msgId);

    void finishActivity();

    void showExitHintLayout();

    void dismissRefresh();
}
