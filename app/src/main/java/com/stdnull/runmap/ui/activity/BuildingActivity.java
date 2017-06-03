package com.stdnull.runmap.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.model.LatLng;
import com.stdnull.runmap.R;
import com.stdnull.runmap.modules.map.AmapWrapper;
import com.stdnull.runmap.presenter.action.IBuildPresenter;
import com.stdnull.runmap.presenter.actionImpl.BuildPresenterImpl;
import com.stdnull.runmap.ui.uibehavior.IBuildActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 停留地点分布图页面
 * Created by chen on 2017/1/28.
 */

public class BuildingActivity extends BaseActivity implements IBuildActivity {
    private IBuildPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        AMap mAmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        AmapWrapper amapWrapper = new AmapWrapper(mAmap);
        amapWrapper.initMap();
        mPresenter = new BuildPresenterImpl(this, amapWrapper);
        mPresenter.moveMapCamera();
        mPresenter.genarateGraph();
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


    @Override
    public void showEmptyLayout() {
        TextView textView = new TextView(BuildingActivity.this);
        textView.setText(R.string.string_empty_tips);
        textView.setTextSize(30);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        ((ViewGroup)(findViewById(android.R.id.content))).addView(textView);
    }
}
