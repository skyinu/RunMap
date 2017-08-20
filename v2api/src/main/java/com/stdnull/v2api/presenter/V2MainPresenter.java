package com.stdnull.v2api.presenter;

import android.os.Bundle;
import android.widget.Toast;
import com.stdnull.baselib.common.CFLog;
import com.stdnull.v2api.api.ApiImpl;
import com.stdnull.v2api.mode.V2ExBean;
import com.stdnull.v2api.mode.V2MainFragModel;
import com.stdnull.v2api.ui.uibehaviour.IV2MainFragment;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
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
    public void requestV2Hot(boolean force) {
        if(mV2MainFragModel.getContentListModel() != null && !force){
            mV2MainFragment.showContent(mV2MainFragModel.getContentListModel());
            CFLog.i("V2MainPresenter", "don't need to request data");
            return;
        }
        mV2MainFragment.startRefresh();
        mApiImpl.listHost(new Callback<List<V2ExBean>>() {
            @Override
            public void onResponse(Call<List<V2ExBean>> call, Response<List<V2ExBean>> response) {
                mV2MainFragModel.setContentListModel(response.body());
                mV2MainFragment.showContent(mV2MainFragModel.getContentListModel());
            }

            @Override
            public void onFailure(Call<List<V2ExBean>> call, Throwable t) {
                mV2MainFragment.stopRefresh();
                Toast.makeText(mV2MainFragment.getActivityContext()
                        , "网络错误", Toast.LENGTH_SHORT).show();
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
