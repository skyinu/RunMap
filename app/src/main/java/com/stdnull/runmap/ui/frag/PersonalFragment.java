package com.stdnull.runmap.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stdnull.runmap.R;
import com.stdnull.runmap.ui.activity.BaseActivity;
import com.stdnull.runmap.ui.activity.BuildingActivity;
import com.stdnull.runmap.ui.activity.CalorieActivity;
import com.stdnull.runmap.ui.activity.ReviewActivity;

/**
 * 我的页面-附加功能页
 * Created by chen on 2017/1/28.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener{
    private View mRootView;
    private Button mReviewBtn;
    private Button mMostVisitBtn;
    private Button mDistance2Carlrie;
    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_personal,container,false);
        initView(mRootView);
        return mRootView;
    }

    protected void initView(View root){
        mReviewBtn = (Button) root.findViewById(R.id.track_review);
        mMostVisitBtn = (Button) root.findViewById(R.id.most_stayed);
        mDistance2Carlrie = (Button) root.findViewById(R.id.distance_to_carorie);
        mMostVisitBtn.setOnClickListener(this);
        mReviewBtn.setOnClickListener(this);
        mDistance2Carlrie.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        BaseActivity host = (BaseActivity) getActivity();
        if(host == null  || !isAdded()){
            return;
        }
        Intent intent;
        switch (v.getId()){
            case R.id.track_review:
                intent = new Intent(host, ReviewActivity.class);
                startActivity(intent);
                break;
            case R.id.most_stayed:
                intent = new Intent(host, BuildingActivity.class);
                startActivity(intent);
                break;
            case R.id.distance_to_carorie:
                intent = new Intent(host, CalorieActivity.class);
                startActivity(intent);
                break;
        }
    }
}
