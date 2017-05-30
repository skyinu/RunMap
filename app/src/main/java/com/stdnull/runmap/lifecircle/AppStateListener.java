package com.stdnull.runmap.lifecircle;

import android.content.Context;

public interface AppStateListener {
    void onForeground(Context context);

    void onBackground(Context context);
}