package com.stdnull.v2api.api;

import android.widget.Toast;

import com.stdnull.baselib.GlobalApplication;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by chen on 2017/8/23.
 */

public abstract class BaseCallBack<T> implements Callback<T> {


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(GlobalApplication.getAppContext()
                , "网络错误", Toast.LENGTH_SHORT).show();
    }
}
