package com.stdnull.runmap.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.stdnull.runmap.R;
import com.stdnull.runmap.bean.BuildingPoint;
import com.stdnull.runmap.bean.TrackPoint;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.map.AmLocationManager;
import com.stdnull.runmap.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
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
        initData();
    }

    protected void initData(){
        CFAsyncTask<List<BuildingPoint>> task = new CFAsyncTask<List<BuildingPoint>>() {
            @Override
            public List<BuildingPoint> onTaskExecuted(Object... params) {
                List<String> dateList = DataManager.getInstance().queryDataTime();
                if(dateList.isEmpty()){
                    return null;
                }
                return calculateTime(dateList);
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
            Map<Integer,List<TrackPoint>> trackItem = DataManager.getInstance().readTrackPointFormDataBase(dateList.get(i));
            Set<Integer> keys = trackItem.keySet();
            for(Integer k : keys){
                List<TrackPoint> trackPoints = trackItem.get(k);
                String buildName = null;
                Long time = 0L;
                for(int j=0;j<trackPoints.size();j++){
                    TrackPoint point = trackPoints.get(j);
                    if(StringUtils.isEmpty(point.getBuildName())){
                        continue;
                    }
                    if(StringUtils.isEmpty(buildName)){
                        buildName = point.getBuildName();
                        time = point.getTimeStamp();
                    }
                    else if(!buildName.equals(point.getBuildName()) || j == trackPoints.size()-1){
                        BuildingPoint buildingPoint = new BuildingPoint(point.getLocation(),point.getBuildName());
                        buildingPoint.setTime(point.getTimeStamp() - time);
                        int index = pointList.indexOf(buildingPoint);
                        if(index >= 0){
                            BuildingPoint item = pointList.get(index);
                            item.setTime(item.getTime()+buildingPoint.getTime());
                        }
                        else{
                            pointList.add(buildingPoint);
                        }
                    }
                }
            }
        }
        return pointList;

    }
}
