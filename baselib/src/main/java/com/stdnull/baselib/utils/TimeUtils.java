package com.stdnull.baselib.utils;

import com.stdnull.baselib.GlobalApplication;
import com.stdnull.baselib.R;

/**
 * Created by chen on 2017/8/23.
 */

public class TimeUtils {
    public static String calculateTimeGap(long anchor){
        long gap = System.currentTimeMillis() - anchor * 1000;
        long gapms = gap/1000;
        if(gapms < 60){
            return GlobalApplication.getAppContext().getString(R.string.v2_time_gap_hint_second, gapms);
        }
        else if(gapms < 60 * 100){
            return GlobalApplication.getAppContext().getString(R.string.v2_time_gap_hint_minute, gapms/60);
        }
        else if(gapms < 24 * 60 * 60){
            return GlobalApplication.getAppContext().
                    getString(R.string.v2_time_gap_hint_hour, gapms/60/60, gapms/60%60);

        }
        else{
            return GlobalApplication.getAppContext().
                    getString(R.string.v2_time_gap_hint_long, gapms/60/60/24);

        }

    }
}
