package com.stdnull.runmap.modules.map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chen on 2017/6/3.
 */

public class MapDrawer {
    private AMap mAmap;

    public MapDrawer(AMap map) {
        this.mAmap = map;
    }

    public void drawPolyLine(float speed, LatLng... latLngs) {
        List<LatLng> tmp = Arrays.asList(latLngs);

        drawPolyLine(tmp, getColorBySpeed(speed));
    }

    public void drawPolyLine(List<LatLng> latLngs, int color) {
        CFLog.e("TAG", "draw new Poly");
        PolylineOptions options = new PolylineOptions();
        options.addAll(latLngs);
        options.color(color);
        options.width(20);
        mAmap.addPolyline(options);
    }

    public int getColorBySpeed(float speed){
        float kmh = speed * 3.6F;
        float fraction = kmh/90;
        int color = evaluate(fraction, RMConfiguration.LOW_SPEED_COLOR,RMConfiguration.HIGH_SPEED_COLOR);
        return  color;
    }

    private int evaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int)(fraction * (endA - startA))) << 24) |
                ((startR + (int)(fraction * (endR - startR))) << 16) |
                ((startG + (int)(fraction * (endG - startG))) << 8) |
                ((startB + (int)(fraction * (endB - startB))));
    }
}
