package com.stdnull.runmap.presenter.actionImpl;

import android.view.View;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.model.IReview;
import com.stdnull.runmap.model.ReviewModel;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.modules.map.IMap;
import com.stdnull.runmap.presenter.action.IReviewPresenter;
import com.stdnull.runmap.ui.activity.IReviewActivity;
import com.stdnull.runmap.ui.activity.ReviewActivity;
import com.stdnull.runmap.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/6/3.
 */

public class ReviewPresenterImpl implements IReviewPresenter {

    private IMap mapInstance;
    private IReviewActivity mReviewActivity;
    private IReview mReviewModel;
    /**
     * 指示当前动画数据index
     */
    private int mSegmentIndex;
    private List<List<LatLng>> mDrawSource;

    private boolean isFirstAnimation = true;


    public ReviewPresenterImpl(IMap mapInstance, IReviewActivity reviewActivity) {
        this.mapInstance = mapInstance;
        this.mReviewActivity = reviewActivity;
        this.mReviewModel = new ReviewModel();
    }

    @Override
    public void initTrackPoints() {
        CFAsyncTask<List<String>> task = new CFAsyncTask<List<String>>() {
            @Override
            public List<String> onTaskExecuted(Object... params) {
                List<String> dateList = DataManager.getInstance().queryDataTime();
                if(dateList.isEmpty()){
                    return null;
                }
                mReviewModel.setCurrentShowData(DataManager.getInstance().readTrackPointFormDataBase(dateList.get(0)));
                if(dateList.size() > 1){
                    mReviewModel.setPreviewData(DataManager.getInstance().readTrackPointFormDataBase(dateList.get(1)));
                }
                return dateList;
            }

            @Override
            public void onTaskFinished(List<String> result) {
                List<String> dateList = result;
                //还没有数据记录
                if(dateList == null || dateList.isEmpty()){
                    mReviewActivity.showEmptyView();
                    return;
                }
                mReviewModel.setDateList(dateList);
                //通知生成轨迹图
                mReviewActivity.updateDateTitle(StringUtils.splitDate(dateList.get(0)));
                mReviewModel.setCurrentPosition(0);
                prepareAnimation(mReviewModel.formatTrackPoints());
                if(dateList.size() <= 1){
                    mReviewActivity.setLeftArrowVisibility(View.INVISIBLE);
                }

            }
        };
        TaskHanler.getInstance().sendTask(task);
    }

    @Override
    public void onLeftArrowClick() {
        mReviewModel.setCurrentPosition(mReviewModel.getCurrentPosition() + 1);
        List<String> dateList = mReviewModel.getDateList();
        mReviewActivity.updateArrowState(ReviewActivity.DIRECT_LEFT,
                mReviewModel.getCurrentPosition(), dateList.size());
        mReviewActivity.updateDateTitle(StringUtils.splitDate(dateList.get(mReviewModel.getCurrentPosition())));
        mReviewModel.setNextData(mReviewModel.getCurrentShowData());
        mReviewModel.setCurrentShowData(mReviewModel.getPreviewData());
        prepareAnimation(mReviewModel.formatTrackPoints());
        if(mReviewModel.getCurrentPosition() < dateList.size() - 1 ){
            updatePrevCache(dateList.get(mReviewModel.getCurrentPosition() + 1));
        }
    }

    @Override
    public void onRightArrowClick() {
        mReviewModel.setCurrentPosition(mReviewModel.getCurrentPosition() - 1);
        List<String> dateList = mReviewModel.getDateList();
        mReviewActivity.updateArrowState(ReviewActivity.DIRECT_RIGHT,
                mReviewModel.getCurrentPosition(), dateList.size());
        mReviewActivity.updateDateTitle(StringUtils.splitDate(dateList.get(mReviewModel.getCurrentPosition())));
        mReviewModel.setPreviewData(mReviewModel.getCurrentShowData());
        mReviewModel.setCurrentShowData(mReviewModel.getNextData());
        prepareAnimation(mReviewModel.formatTrackPoints());
        if(mReviewModel.getCurrentPosition() > 0 ){
            updateNextCache(dateList.get(mReviewModel.getCurrentPosition() - 1));
        }
    }

    private void updatePrevCache(final String date){
        CFAsyncTask<Map<Integer,List<TrackPoint>>> task = new CFAsyncTask<Map<Integer, List<TrackPoint>>>() {
            @Override
            public Map<Integer, List<TrackPoint>> onTaskExecuted(Object... params) {
                return DataManager.getInstance().readTrackPointFormDataBase(date);
            }

            @Override
            public void onTaskFinished(Map<Integer, List<TrackPoint>> result) {
                mReviewModel.setPreviewData(result);
            }
        };
        TaskHanler.getInstance().sendTask(task);
    }

    private void updateNextCache(final String date){
        CFAsyncTask<Map<Integer,List<TrackPoint>>> task = new CFAsyncTask<Map<Integer, List<TrackPoint>>>() {
            @Override
            public Map<Integer, List<TrackPoint>> onTaskExecuted(Object... params) {
                return DataManager.getInstance().readTrackPointFormDataBase(date);
            }

            @Override
            public void onTaskFinished(Map<Integer, List<TrackPoint>> result) {
                mReviewModel.setNextData(result);
            }
        };
        TaskHanler.getInstance().sendTask(task);
    }

    private void prepareAnimation(final List<List<LatLng>> drawSource){
        mapInstance.clear();
        this.mDrawSource = drawSource;
        mSegmentIndex = 0;
        if(isFirstAnimation) {
            isFirstAnimation = false;
            TaskHanler.getInstance().sendTaskDelayed(new Runnable() {
                @Override
                public void run() {
                    mapInstance.moveToSpecficCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(drawSource.get(mSegmentIndex).get(0), 16, 0, 0)));
                }
            }, 1000);
        }
        else {
            mapInstance.moveToSpecficCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(drawSource.get(mSegmentIndex).get(0), 16, 0, 0)));
        }

        mapInstance.drawTrackAnimation(drawSource.get(mSegmentIndex), mSegmentIndex, new TrackLineMoveListener());
    }

    class  TrackLineMoveListener implements SmoothMoveMarker.MoveListener{
        private boolean hasExecuted = false;
        @Override
        public void move(double v) {
            CFLog.e("TAG","remain distance ="+v );
            if(!hasExecuted && v == 0 && mSegmentIndex < mDrawSource.size()){
                CFLog.e("TAG","a line finished, prepare to draw new line");
                hasExecuted = true;
                mSegmentIndex ++;
                mapInstance.drawTrackAnimation(mDrawSource.get(mSegmentIndex), mSegmentIndex,
                        new TrackLineMoveListener());
            }
        }
    }
}
