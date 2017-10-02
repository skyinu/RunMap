package com.stdnull.v2api.presenter;

import android.os.Bundle;

import com.stdnull.baselib.common.CFLog;
import com.stdnull.v2api.api.ApiImpl;
import com.stdnull.v2api.model.V2ExBean;
import com.stdnull.v2api.model.V2MainFragModel;
import com.stdnull.v2api.ui.uibehaviour.IV2MainFragment;

import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
            mV2MainFragment.showContent(mV2MainFragModel.getContentListModel());
            CFLog.i("V2MainPresenter", "don't need to request data");
            return;
        }
        mV2MainFragment.startRefresh();
        mV2MainFragModel.clearModel();
        Observable.just(mApiImpl.listHost(), mApiImpl.listLastest())
                .subscribeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mV2MainFragment.showContent(mV2MainFragModel.getContentListModel());
                    }
                })
                .map(new Func1<Call<List<V2ExBean>>, List<V2ExBean>>() {
                    @Override
                    public List<V2ExBean> call(Call<List<V2ExBean>> listCall) {
                        try {
                            return listCall.execute().body();
                        } catch (IOException e) {
                            CFLog.e("V2MainPresenter", "network error");
                        }
                        return null;
                    }
                })
                .filter(new Func1<List<V2ExBean>, Boolean>() {
                    @Override
                    public Boolean call(List<V2ExBean> v2ExBeans) {
                        if(v2ExBeans == null){
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseAction(), new ErrorAction(), new CompleteAction());
    }

    class ResponseAction implements Action1<List<V2ExBean>>{

        @Override
        public void call(List<V2ExBean> v2ExBeans) {
            mV2MainFragModel.addContentListModel(v2ExBeans);
        }
    }

    class ErrorAction implements Action1<Throwable>{

        @Override
        public void call(Throwable throwable) {
            mV2MainFragment.stopRefresh();
        }
    }

    class CompleteAction implements Action0{

        @Override
        public void call() {
            mV2MainFragment.stopRefresh();
            mV2MainFragment.showContent(mV2MainFragModel.getContentListModel());
        }
    }

    public void save(Bundle bundle){
        mV2MainFragModel.save(bundle);
    }
    public boolean restore(Bundle bundle){
        return mV2MainFragModel.restore(bundle);
    }
}
