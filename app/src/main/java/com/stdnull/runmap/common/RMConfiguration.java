package com.stdnull.runmap.common;

/**
 * Created by chen on 2016/12/26.
 */

public final class RMConfiguration {
    public static final String FILE_CONFIG = "rm_config";
    public static final String KEY_VERSION = "rm_version";

    public static final int SECOND = 1000;//秒的时间长度定义
    public static final int HTTP_OUT_TIME = 30 * SECOND;//HTTP请求超时时间
    public static final int DRAW_DISTANCE = 10;//最短绘制距离

    public static final String DATABASE_NAME = "location";

    public static final int MAX_SPEED = 40;

    public static final int MIN_TIME_STAYED = 10 * 60 * SECOND;

    public static final int MAX_SUPPORT_ITEMS = 30;

}
