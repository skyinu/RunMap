package com.stdnull.runmap.modules.map;

import android.graphics.Color;
import android.os.SystemClock;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.model.BuildingPoint;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Created by chen on 2017/6/3.
 */

public class MapDrawer {
    private AMap mAmap;
    private Queue<SmoothMoveMarker> mMarkerLists = new LinkedList<>();

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

    public void drawTrackAnimation(List<LatLng> drawSource, int currentIndex, SmoothMoveMarker.MoveListener moveListener) {
        //寻找与起点距离最远的点
        SmoothMoveMarker pre = mMarkerLists.peek();
        if(pre != null){
            pre.setMoveListener(null);
            mMarkerLists.poll();
        }
        float maxDistance = 0;
        LatLng endPoint = null;
        for (int i = 1; i < drawSource.size(); i++) {
            float distance = AMapUtils.calculateLineDistance(drawSource.get(0), drawSource.get(i));
            if (distance > maxDistance) {
                endPoint = drawSource.get(i);
                maxDistance = distance;
            }
        }
        CFLog.e("TAG", "max distance = " + maxDistance);

        //代表构成的一个矩形区域，由两点决定
        LatLngBounds bounds = new LatLngBounds(drawSource.get(0), endPoint);

        float pad = GlobalApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity * RMConfiguration.MAP_PADDING;
        mAmap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(drawSource.get(0), 17, 0, 0)));
        mAmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, (int) pad));

        drawSingleMaker(drawSource.get(0), GlobalApplication.getAppContext().getString(R.string.string_start_point), -1);
        drawSingleMaker(drawSource.get(drawSource.size() - 1), GlobalApplication.getAppContext().getString(R.string.string_end_point), -1);
        if (currentIndex == 0) {
            drawPolyLineWithTexture(drawSource, R.mipmap.track_line_texture);
        } else {
            Random random = new Random(SystemClock.currentThreadTimeMillis());

            drawPolyLine(drawSource, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        }


        //按照指定的经纬度数据和时间，平滑移动
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(mAmap);
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.track_line_icon));
        // 设置滑动的轨迹点
        smoothMarker.setPoints(drawSource);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(20);
        //设置监听
        smoothMarker.setMoveListener(moveListener);
        // 开始滑动
        smoothMarker.startSmoothMove();
        mMarkerLists.add(smoothMarker);
    }

    public void drawPolyLineWithTexture(List<LatLng> latLngs, int textureId) {
        mAmap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(textureId))
                .addAll(latLngs)
                .useGradient(true)
                .width(18));
    }

    public void drawSingleMaker(LatLng latLng, String title, int iconId) {
        if (iconId != -1) {
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            options.icon(BitmapDescriptorFactory.fromResource(iconId));
            mAmap.addMarker(options);
        } else {
            mAmap.addMarker(new MarkerOptions().position(latLng).title(title).snippet("DefaultMarker"));
        }
    }

    public void drawMarkers(List<BuildingPoint> buildingPointList) {
        for (int i = 0; i < buildingPointList.size(); i++) {
            BuildingPoint point = buildingPointList.get(i);
            MarkerOptions options = new MarkerOptions().position(point.getLatLng()).title(point.getBuildName());
            options.snippet("停留" + (point.getTime() / 1000 / 60) + "分钟");
            options.setFlat(true);
            Marker marker = mAmap.addMarker(options);
            marker.setInfoWindowEnable(true);
            marker.showInfoWindow();
        }
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
