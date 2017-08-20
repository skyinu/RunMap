package com.stdnull.v2api.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stdnull.v2api.R;
import com.stdnull.v2api.R2;
import com.stdnull.v2api.injection.components.DaggerV2MainComponent;
import com.stdnull.v2api.injection.modules.V2MainModule;
import com.stdnull.v2api.mode.V2ExBean;
import com.stdnull.v2api.presenter.V2MainPresenter;
import com.stdnull.v2api.ui.adapter.ContentListAdapter;
import com.stdnull.v2api.ui.uibehaviour.IV2MainFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chen on 2017/8/20.
 */

public class V2MainFragment extends Fragment implements IV2MainFragment{
    private View mRootView;
    @BindView(R2.id.v2_content_list_view)
    RecyclerView mContentRecycleView;
    @Inject
    V2MainPresenter mV2MainPresenter;
    @Inject
    ContentListAdapter mContentListAdapter;
    public static V2MainFragment newInstance() {
        V2MainFragment fragment = new V2MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerV2MainComponent.
                builder()
                .v2MainModule(new V2MainModule(this))
                .build().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mV2MainPresenter.restore(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.v2_main_fragment_layout, container, false);
        ButterKnife.bind(this, mRootView);
        mContentRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentRecycleView.setAdapter(mContentListAdapter);
        mV2MainPresenter.requestV2Hot();
        return mRootView;
    }

    @Override
    public void showContent(List<V2ExBean> content) {
        mContentListAdapter.setContents(content);
        mContentListAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }
}
