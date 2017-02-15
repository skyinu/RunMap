package com.stdnull.runmap.map;

import android.graphics.Bitmap;

/**
 * 截图完成回调
 * Created by chen on 2017/2/10.
 */

public interface OnCaptureListener {
    void onCaptureFinished(Bitmap bitmap,int status);
}
