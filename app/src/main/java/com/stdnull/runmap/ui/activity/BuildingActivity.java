package com.stdnull.runmap.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.R;
import com.stdnull.runmap.model.BuildingPoint;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.RMConfiguration;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.modules.map.AmLocationManager;
import com.stdnull.runmap.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 停留地点分布图页面
 * Created by chen on 2017/1/28.
 */

public class BuildingActivity extends BaseActivity {
    private List<BuildingPoint> mBuildPoints;
    private AMap mAmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        mAmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        AmLocationManager.getInstance().initAMap(mAmap,1);
        CameraUpdate update = CameraUpdateFactory.zoomTo(4);
        AmLocationManager.getInstance().moveToSpecficCamera(update);
        initData();
    }

    public static List<LatLng> test = new ArrayList<>();

    static {
        test.add(new LatLng(31.7029985115958, 117.92576410224807));
        test.add(new LatLng(32.7029985115958, 120.92576410224807));
        test.add(new LatLng(34.7029985115958, 119.92576410224807));
        test.add(new LatLng(31.7029985115958, 118.92576410224807));
        test.add(new LatLng(33.7029985115958, 116.92576410224807));
        test.add(new LatLng(36.7029985115958, 122.92576410224807));
        test.add(new LatLng(37.7029985115958, 123.92576410224807));
        test.add(new LatLng(38.7029985115958, 124.92576410224807));
        test.add(new LatLng(39.7029985115958, 125.92576410224807));
        test.add(new LatLng(40.7029985115958, 126.92576410224807));
        test.add(new LatLng(30.7029985115958, 115.92576410224807));
        test.add(new LatLng(32.7029985115958, 118.92576410224807));


    }

    protected void initData(){
        CFAsyncTask<List<BuildingPoint>> task = new CFAsyncTask<List<BuildingPoint>>() {
            @Override
            public List<BuildingPoint> onTaskExecuted(Object... params) {
                List<String> dateList = DataManager.getInstance().queryDataTime();
                if(dateList.isEmpty()){
                    return null;
                }
                List<BuildingPoint> res = calculateTime(dateList);
                CFLog.e("Build","point = "+res.toString());
                for(int i=0;i<res.size();i++){
                    BuildingPoint point = res.get(i);
                    if(point.getTime() <RMConfiguration.MIN_TIME_STAYED){
                        res.remove(i);
                        i--;
                    }
                }
                CFLog.e("Build","point = "+res.toString());
                return res;
            }

            @Override
            public void onTaskFinished(List<BuildingPoint> result) {
                mBuildPoints = result;
                //还没有数据记录
                if(mBuildPoints == null || mBuildPoints.isEmpty()){
                    TextView textView = new TextView(BuildingActivity.this);
                    textView.setText(R.string.string_empty_tips);
                    textView.setTextSize(30);
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setGravity(Gravity.CENTER);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    textView.setLayoutParams(lp);
                    ((ViewGroup)(findViewById(android.R.id.content))).addView(textView);
                    return;
                }

//                for (int i=0;i<test.size();i++){
//                    BuildingPoint point =new BuildingPoint(test.get(i),"模拟地点 "+i);
//                    point.setTime(600000);
//                    mBuildPoints.add(point);
//                }
                AmLocationManager.getInstance().drawMarkers(mBuildPoints);

            }
        };
        TaskHanler.getInstance().sendTask(task);
    }

    /**
     * 按Building计算停留地点
     * @param dateList
     * @return
     */
    private List<BuildingPoint> calculateTime(List<String> dateList){
        List<BuildingPoint> pointList = new ArrayList<>();
        for(int i=0;i<dateList.size();i++){
            //按日期取数据
            Map<Integer,List<TrackPoint>> trackItem = DataManager.getInstance().readTrackPointFormDataBase(dateList.get(i));
            Set<Integer> keys = trackItem.keySet();
            //按日期遍历记录次数
            for(Integer k : keys){
                List<TrackPoint> trackPoints = trackItem.get(k);
                String buildName = null;
                Long time = 0L;
                boolean startFlag = false;
                TrackPoint anchorPint = null;
                //按次数读取经纬度信息
                for(int j=0;j<trackPoints.size();j++){
                    TrackPoint point = trackPoints.get(j);
                    //如果当前记录的buildName为空,开始新的计算
                    if(!startFlag){
                        startFlag = true;
                        buildName = point.getBuildName();
                        time = point.getTimeStamp();
                        anchorPint = point;
                    }
                    else if(!isClosed(anchorPint,point)|| j == trackPoints.size()-1){
                        TrackPoint anchor = trackPoints.get(j-1);
                        //使用前一个点作为坐标点
                        BuildingPoint buildingPoint = new BuildingPoint(anchor.getLocation(),buildName);
                        buildingPoint.setTime(point.getTimeStamp() - time);
                        int index = pointList.indexOf(buildingPoint);
                        //合并重复
                        if(index >= 0){
                            BuildingPoint item = pointList.get(index);
                            item.setTime(item.getTime()+buildingPoint.getTime());
                        }
                        else{
                            pointList.add(buildingPoint);
                        }
                        //非最后数据点需再计算
                        if(j != trackPoints.size() - 1){
                            j--;
                        }
                        buildName = null;
                        startFlag = false;
                    }
                    else{
                        if(!StringUtils.isEmpty(point.getBuildName())) {
                            buildName = point.getBuildName();
                        }
                    }
                    //如果相同继续遍历计算
                }
            }
        }
        return pointList;
    }

    private boolean isClosed(TrackPoint cur,TrackPoint cmp){
        float distance = AMapUtils.calculateLineDistance(cur.getLocation(),cmp.getLocation());
        if(distance < RMConfiguration.MAX_DISTANCE){
            CFLog.e("Build","closed = "+distance);
            return true;
        }
        return false;
    }
}
