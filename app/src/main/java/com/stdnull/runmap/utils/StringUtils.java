package com.stdnull.runmap.utils;

/**
 * Created by chen on 2017/1/20.
 */

public final class StringUtils {
    public static boolean isEmpty(String string){
        if(string == null || string.length() < 1){
            return true;
        }
        return false;
    }
}
