package com.stdnull.runmap.presenter.actionImpl;

import android.util.SparseArray;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.stdnull.baselib.common.CFLog;
import com.stdnull.baselib.common.CFAsyncTask;
import com.stdnull.baselib.common.RMConfiguration;
import com.stdnull.baselib.common.TaskHanler;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.model.BuildingPoint;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.modules.map.IMap;
import com.stdnull.runmap.presenter.action.IBuildPresenter;
import com.stdnull.runmap.ui.uibehavior.IBuildActivity;
import com.stdnull.baselib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chen on 2017/6/3.
 */

public class BuildPresenterImpl implements IBuildPresenter {
    private IMap mapInstance;
    private IBuildActivity mBuildActivity;
    private List<BuildingPoint> mBuildPoints;

    public BuildPresenterImpl(IBuildActivity buildActivity, IMap mapInstance) {
        this.mBuildActivity = buildActivity;
        this.mapInstance = mapInstance;
    }

    @Override
    public void moveMapCamera() {
        TaskHanler.getInstance().sendTaskDelayed(new Runnable() {
            @Override
            public void run() {
                CameraUpdate update = CameraUpdateFactory.zoomTo(4);
                mapInstance.moveToSpecficCamera(update);
            }
        }, 1000);


    }

    @Override
    public void genarateGraph() {
        Observable.fromCallable(new Callable<List<BuildingPoint>>() {
            @Override
            public List<BuildingPoint> call() throws Exception {
                List<String> dateList = DataManager.getInstance().queryDataTime();
                if(dateList.isEmpty()){
                    return null;
                }
                List<BuildingPoint> res = calculateTime(dateList);
                CFLog.e("Build","point = "+res.toString());
                for(int i=0;i<res.size();i++){
                    BuildingPoint point = res.get(i);
                    if(point.getTime() < RMConfiguration.MIN_TIME_STAYED){
                        res.remove(i);
                        i--;
                    }
                }
                CFLog.e("Build","point = "+res.toString());
                return res;
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BuildingPoint>>() {
                    @Override
                    public void call(List<BuildingPoint> buildingPoints) {
                        mBuildPoints = buildingPoints;
                        //还没有数据记录
                        if(mBuildPoints == null || mBuildPoints.isEmpty()){
                            mBuildActivity.showEmptyLayout();
                            return;
                        }
                        mapInstance.drawMarkers(mBuildPoints);
                    }
                });
    }

    /**
     * 按Building计算停留地点
     * @param dateList
     * @return
     */
    private List<BuildingPoint> calculateTime(List<String> dateList){
        List<BuildingPoint> pointList = new ArrayList<>();
        for(int i = 0;i < dateList.size();i++){
            //按日期取数据
            SparseArray<List<TrackPoint>> trackItem = DataManager.getInstance().readTrackPointFormDataBase(dateList.get(i));
            //按日期遍历记录次数
            for(int keyIndex = 0; keyIndex < trackItem.size(); keyIndex++){
                List<TrackPoint> trackPoints = trackItem.get(trackItem.keyAt(keyIndex));
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
