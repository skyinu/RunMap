package com.stdnull.v2api.api;

import com.stdnull.v2api.mode.V2ExBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chen on 2017/8/20.
 */

public class ApiImpl {
    public static final String BASE_URL = "https://www.v2ex.com";
    private Retrofit retrofit;

    public ApiImpl(){
        retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void listHost(Callback<List<V2ExBean>> callback){
        V2Hot v2Hot = retrofit.create(V2Hot.class);
        Call<List<V2ExBean>> call = v2Hot.listHost();
        call.enqueue(callback);
        return;
    }

    public Call<ResponseBody> listLastest(){
        return null;
    }
}
