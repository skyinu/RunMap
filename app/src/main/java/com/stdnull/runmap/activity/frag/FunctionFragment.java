package com.stdnull.runmap.activity.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stdnull.runmap.GlobalApplication;
import com.stdnull.runmap.R;
import com.stdnull.runmap.activity.BaseActivity;
import com.stdnull.runmap.activity.TrackActivity;
import com.stdnull.runmap.common.CFLog;
import com.stdnull.runmap.utils.SystemUtils;

/**
 * Created by chen on 2017/1/28.
 */

public class FunctionFragment extends Fragment implements View.OnClickListener{
    private View mRootView;
    private TextView mTvTotalTrack;
    private Button mStartTrackBtn;
    public static FunctionFragment newInstance() {
        FunctionFragment fragment = new FunctionFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_function,container,false);
        initView(mRootView);
        return mRootView;
    }

    protected void initView(View root){
        mStartTrackBtn = (Button) root.findViewById(R.id.btn_start_track);
        mTvTotalTrack = (TextView) root.findViewById(R.id.tv_total_track);
        mStartTrackBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_track:
                startTrackActivity();
                break;
        }
    }

    private void startTrackActivity(){
        BaseActivity host = (BaseActivity) getActivity();
        if(host == null){
            CFLog.e(this.getClass().getName(),"Activity has detached");
            return;
        }
        if(SystemUtils.isGpsEnabled(GlobalApplication.getAppContext())){
            if(host != null) {
                Intent intent = new Intent(host, TrackActivity.class);
                startActivity(intent);
            }
        }
        else{
            host.showSettingDialog(Settings.ACTION_LOCATION_SOURCE_SETTINGS, host.getString(R.string.need_gps));
        }
    }
}
