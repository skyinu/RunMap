package com.stdnull.v2api.api;

import com.stdnull.v2api.model.V2ExBean;

import java.util.List;
import retrofit2.Call;
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

    public Call<List<V2ExBean>> listHost(){
        V2Hot v2Hot = retrofit.create(V2Hot.class);
        return v2Hot.listHost();
    }

    public Call<List<V2ExBean>> listLastest(){
        V2Lastest v2Lastest = retrofit.create(V2Lastest.class);
        return v2Lastest.listLastest();
    }
}
