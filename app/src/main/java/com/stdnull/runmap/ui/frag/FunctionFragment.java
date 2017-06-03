package com.stdnull.runmap.ui.frag;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stdnull.runmap.R;
import com.stdnull.runmap.presenter.actionImpl.FunctionFragPresenterImpl;
import com.stdnull.runmap.presenter.action.IFunctionFragPresenter;
import com.stdnull.runmap.ui.activity.BaseActivity;
import com.stdnull.runmap.ui.uibehavior.IFunctionFragment;

/**
 * 主功能页面
 * Created by chen on 2017/1/28.
 */

public class FunctionFragment extends Fragment implements View.OnClickListener, IFunctionFragment {
    private View mRootView;
    private TextView mTvTotalTrack;
    private Button mStartTrackBtn;
    private IFunctionFragPresenter mPresenter;
    public static FunctionFragment newInstance() {
        FunctionFragment fragment = new FunctionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FunctionFragPresenterImpl(this);
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
        Activity host = getActivity();
        mPresenter.updateTotalDistance(host);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_track:
                mPresenter.startTrackActivity((BaseActivity) getActivity());
                break;
        }
    }

    @Override
    public void showUpgradeDistance(String distance) {
        mTvTotalTrack.setText(distance);
    }
}
