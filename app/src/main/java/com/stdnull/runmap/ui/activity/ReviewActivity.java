package com.stdnull.runmap.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.stdnull.runmap.R;
import com.stdnull.runmap.model.TrackPoint;
import com.stdnull.runmap.common.CFAsyncTask;
import com.stdnull.runmap.common.TaskHanler;
import com.stdnull.runmap.managers.DataManager;
import com.stdnull.runmap.modules.map.AmLocationManager;
import com.stdnull.runmap.modules.map.AmReviewTrackHelper;
import com.stdnull.runmap.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 轨迹回放页面
 * Created by chen on 2017/1/28.
 */

public class ReviewActivity extends BaseActivity implements View.OnClickListener{
    private List<String> mDateList;
    private TextView mTvTime;
    private ImageView mRightArrow;
    private ImageView mLeftArrow;

    private AMap mAmap;
    private AmReviewTrackHelper mHelper;


    private Map<Integer,List<TrackPoint>> mPreview;
    private Map<Integer,List<TrackPoint>> mCurrentShow;
    private Map<Integer,List<TrackPoint>> mNext;

    private int mCurrentIndex = 0;

    private static final int DIRECT_LEFT = 0x1;
    private static final int DIRECT_RIGHT = 0X2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mTvTime = (TextView) findViewById(R.id.data_time);
        mRightArrow = (ImageView) findViewById(R.id.right_arrow);
        mLeftArrow = (ImageView) findViewById(R.id.left_arrow);
        mRightArrow.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);
        mAmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        AmLocationManager.getInstance().initAMap(mAmap,1);
        initData();
        mRightArrow.setVisibility(View.GONE);
    }

    /**
     * 初始化待处理数据
     */
    private void initData(){
        mHelper = new AmReviewTrackHelper();
        CFAsyncTask<List<String>> task = new CFAsyncTask<List<String>>() {
            @Override
            public List<String> onTaskExecuted(Object... params) {
                List<String> dateList = DataManager.getInstance().queryDataTime();
                if(dateList.isEmpty()){
                    return null;
                }
                mCurrentShow = DataManager.getInstance().readTrackPointFormDataBase(dateList.get(0));
                if(dateList.size() > 1){
                    mPreview = DataManager.getInstance().readTrackPointFormDataBase(dateList.get(1));
                }
                return dateList;
            }

            @Override
            public void onTaskFinished(List<String> result) {
                mDateList = result;
                //还没有数据记录
                if(mDateList == null || mDateList.isEmpty()){
                    TextView textView = new TextView(ReviewActivity.this);
                    textView.setText(R.string.string_empty_tips);
                    textView.setTextSize(30);
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setGravity(Gravity.CENTER);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    textView.setLayoutParams(lp);
                    ((ViewGroup)(findViewById(android.R.id.content))).addView(textView);
                    return;
                }
                //通知生成轨迹图
                mTvTime.setText(StringUtils.splitDate(mDateList.get(0)));
                mCurrentIndex = 0;
                mHelper.notifyNewData(mCurrentShow);
                if(mDateList.size() <= 1){
                    mLeftArrow.setVisibility(View.GONE);
                }

            }
        };
        TaskHanler.getInstance().sendTask(task);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_arrow:
                mCurrentIndex ++;
                updateArrowState(DIRECT_LEFT,mCurrentIndex);
                mTvTime.setText(StringUtils.splitDate(mDateList.get(mCurrentIndex)));
                mNext = mCurrentShow;
                mCurrentShow = mPreview;
                mHelper.notifyNewData(mCurrentShow);
                if(mCurrentIndex < mDateList.size()-1 ){
                    updatePrevCache(mDateList.get(mCurrentIndex+1));
                }
                break;
            case R.id.right_arrow:
                mCurrentIndex--;
                updateArrowState(DIRECT_RIGHT,mCurrentIndex);
                mTvTime.setText(StringUtils.splitDate(mDateList.get(mCurrentIndex)));
                mPreview = mCurrentShow;
                mCurrentShow = mNext;
                mHelper.notifyNewData(mCurrentShow);
                if(mCurrentIndex > 0 ){
                    updateNextCache(mDateList.get(mCurrentIndex-1));
                }
                break;
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
                mPreview = result;
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
                mNext = result;
            }
        };
        TaskHanler.getInstance().sendTask(task);
    }



    private void updateArrowState(int flag,int index){
        if(flag == DIRECT_LEFT){
            if(index == mDateList.size()-1){
                mLeftArrow.setVisibility(View.GONE);
            }
            if(index > 0){
                mRightArrow.setVisibility(View.VISIBLE);
            }
        }
        else{
            if(index == 0){
                mRightArrow.setVisibility(View.GONE);
            }
            if(index < mDateList.size()-1){
                mLeftArrow.setVisibility(View.VISIBLE);
            }
        }
    }
}
