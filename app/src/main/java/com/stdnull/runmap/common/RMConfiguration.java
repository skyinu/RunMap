package com.stdnull.runmap.common;

import android.graphics.Color;

/**
 * 系统配置工具类
 * Created by chen on 2016/12/26.
 */

public final class RMConfiguration {
    public static final String FILE_CONFIG = "rm_config";
    public static final String KEY_VERSION = "rm_version";
    public static final String KEY_TOTAL_DISTANCE = "rm_distance";
    public static final String KEY_TMP_DISTANCE = "rm_temp_distance";

    public static final int SECOND = 1000;//秒的时间长度定义
    public static final int HTTP_OUT_TIME = 30 * SECOND;//HTTP请求超时时间
    public static final int DRAW_DISTANCE = 10;//最短绘制距离

    public static final String DATABASE_NAME = "location";

    public static final int MAX_SPEED = 40;

    //用于计算停留地点的常量
    public static final int MIN_TIME_STAYED = 10 * 60 * SECOND;
    public static final int MAX_DISTANCE = 100;

    public static final int MAX_SUPPORT_ITEMS = 30;

    public static final int MIN_CACHE_DATA = 10;

    public static final int MAP_PADDING = 120;

    public static final String WEIXIN_APP_ID = "wx80d09d1ee6a69b27";

    public static final int FORCE_COUNT = 60;

    public static final int LOW_SPEED_COLOR = Color.parseColor("#06ff00");
    public static final int HIGH_SPEED_COLOR = Color.parseColor("#ff0501");

}
