package com.stdnull.v2api.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;
import com.stdnull.baselib.common.CFLog;
import com.stdnull.v2api.R;
import com.stdnull.v2api.R2;
import com.stdnull.v2api.injection.components.DaggerV2MainComponent;
import com.stdnull.v2api.injection.modules.V2MainModule;
import com.stdnull.v2api.model.V2ExBean;
import com.stdnull.v2api.presenter.V2MainPresenter;
import com.stdnull.v2api.ui.adapter.ContentListAdapter;
import com.stdnull.v2api.ui.uibehaviour.IV2MainFragment;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by chen on 2017/8/20.
 */

public class V2MainFragment extends Fragment implements IV2MainFragment, SwipeRefreshLayout.OnRefreshListener {
    private View mRootView;
    @BindView(R2.id.v2_content_list_view)
    RecyclerView mContentRecycleView;
    @BindView(R2.id.v2_refresh_view)
    SwipeRefreshLayout mRefreshLayout;
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
        mV2MainPresenter.restore(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mV2MainPresenter.save(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.v2_main_fragment_layout, container, false);
        GradleButterKnife.bind(this, mRootView);
        initUI();
        return mRootView;
    }

    private void initUI(){
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);
        mContentRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mContentRecycleView.setAdapter(mContentListAdapter);
        mV2MainPresenter.requestV2FeedData(false);
        mContentRecycleView.addOnScrollListener(new TopItemListener());
    }

    @Override
    public void showContent(List<V2ExBean> content) {
        if(content.isEmpty()){
            return;
        }
        mContentListAdapter.setContents(content);
        mContentListAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void startRefresh() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mV2MainPresenter.requestV2FeedData(true);
    }

    class TopItemListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                if(firstItemPosition == 0 || mContentListAdapter.getItemCount() == 0){
                    mRefreshLayout.setEnabled(true);
                    CFLog.e("V2MainFragment", "arrive top");
                }
                else{
                    mRefreshLayout.setEnabled(false);
                }
            }
        }
    }
}
