package com.stdnull.v2api.presenter;

import android.os.Bundle;

import com.stdnull.baselib.common.CFLog;
import com.stdnull.v2api.api.ApiImpl;
import com.stdnull.v2api.api.BaseCallBack;
import com.stdnull.v2api.model.V2ExBean;
import com.stdnull.v2api.model.V2MainFragModel;
import com.stdnull.v2api.ui.uibehaviour.IV2MainFragment;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chen on 2017/8/20.
 */

public class V2MainPresenter {
    private IV2MainFragment mV2MainFragment;
    private ApiImpl mApiImpl;
    private V2MainFragModel mV2MainFragModel;
    @Inject
    public V2MainPresenter(IV2MainFragment v2MainFragment){
        this.mV2MainFragment = v2MainFragment;
        this.mApiImpl = new ApiImpl();
        this.mV2MainFragModel = new V2MainFragModel();
    }
    public void requestV2FeedData(boolean force) {
        if(!mV2MainFragModel.isModelEmpty() && !force){
            mV2MainFragment.showContent(mV2MainFragModel.getContentListModel(), false);
            CFLog.i("V2MainPresenter", "don't need to request data");
            return;
        }
        mV2MainFragment.startRefresh();
        mV2MainFragModel.clearModel();
        mApiImpl.listHost(new BaseCallBack<List<V2ExBean>>() {
            @Override
            public void onResponse(Call<List<V2ExBean>> call, Response<List<V2ExBean>> response) {
                boolean stopRefresh = mV2MainFragModel.isModelEmpty();
                mV2MainFragModel.addContentListModel(response.body());
                mV2MainFragment.showContent(mV2MainFragModel.getContentListModel(), !stopRefresh);
            }

            @Override
            public void onFailure(Call<List<V2ExBean>> call, Throwable t) {
                super.onFailure(call, t);
                mV2MainFragment.showContent(mV2MainFragModel.getContentListModel(), true);
            }
        });

        mApiImpl.listLastest(new BaseCallBack<List<V2ExBean>>() {
            @Override
            public void onResponse(Call<List<V2ExBean>> call, Response<List<V2ExBean>> response) {
                boolean stopRefresh = mV2MainFragModel.isModelEmpty();
                mV2MainFragModel.addContentListModel(response.body());
                mV2MainFragment.showContent(mV2MainFragModel.getContentListModel(), !stopRefresh);
            }
            @Override
            public void onFailure(Call<List<V2ExBean>> call, Throwable t) {
                super.onFailure(call, t);
                mV2MainFragment.showContent(mV2MainFragModel.getContentListModel(), true);
            }
        });


    }

    public void save(Bundle bundle){
        mV2MainFragModel.save(bundle);
    }
    public boolean restore(Bundle bundle){
        return mV2MainFragModel.restore(bundle);
    }
}
