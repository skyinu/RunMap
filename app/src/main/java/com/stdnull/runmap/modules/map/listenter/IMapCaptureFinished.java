package com.stdnull.runmap.modules.map.listenter;

import android.graphics.Bitmap;

/**
 * Created by chen on 2017/6/3.
 */

public interface IMapCaptureFinished {
    void onMapCaptureFinished(Bitmap bitmap, int status);
}
