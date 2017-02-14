package com.stdnull.runmap.map;

import android.graphics.Bitmap;

/**
 * Created by chen on 2017/2/10.
 */

public interface OnCaptureListener {
    void onCaptureFinished(Bitmap bitmap,int status);
}
