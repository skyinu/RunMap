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
import com.stdnull.runmap.modules.map.AmapWrapper;
import com.stdnull.runmap.presenter.action.IReviewPresenter;
import com.stdnull.runmap.presenter.actionImpl.ReviewPresenterImpl;

/**
 * 轨迹回放页面
 * Created by chen on 2017/1/28.
 */

public class ReviewActivity extends BaseActivity implements IReviewActivity, View.OnClickListener{
    private TextView mTvTime;
    private ImageView mRightArrow;
    private ImageView mLeftArrow;

    public static final int DIRECT_LEFT = 0x1;
    public static final int DIRECT_RIGHT = 0X2;

    private IReviewPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        bindView();
        initData();
    }

    protected void bindView(){
        mTvTime = (TextView) findViewById(R.id.data_time);
        mRightArrow = (ImageView) findViewById(R.id.right_arrow);
        mLeftArrow = (ImageView) findViewById(R.id.left_arrow);
        mRightArrow.setVisibility(View.GONE);
    }


    private void initData(){
        mRightArrow.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);
        AMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        AmapWrapper wrapper = new AmapWrapper(map);
        wrapper.initMap();
        mPresenter = new ReviewPresenterImpl(wrapper, this);
        mPresenter.initTrackPoints();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_arrow:
                mPresenter.onLeftArrowClick();
                break;
            case R.id.right_arrow:
                mPresenter.onRightArrowClick();
                break;
        }
    }

    @Override
    public void updateArrowState(int direction,int position, int count){
        if(direction == DIRECT_LEFT){
            if(position == count - 1){
                mLeftArrow.setVisibility(View.GONE);
            }
            if(position > 0){
                mRightArrow.setVisibility(View.VISIBLE);
            }
        }
        else{
            if(position == 0){
                mRightArrow.setVisibility(View.GONE);
            }
            if(position < count - 1){
                mLeftArrow.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showEmptyView() {
        TextView textView = new TextView(ReviewActivity.this);
        textView.setText(R.string.string_empty_tips);
        textView.setTextSize(30);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        ((ViewGroup)(findViewById(android.R.id.content))).addView(textView);
    }

    @Override
    public void updateDateTitle(String date) {
        mTvTime.setText(date);
    }

    @Override
    public void setLeftArrowVisibility(int visibility) {
        mLeftArrow.setVisibility(visibility);
    }
}
