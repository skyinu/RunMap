package com.stdnull.v2api.api;

import com.stdnull.v2api.model.V2ExBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chen on 2017/8/20.
 */

public interface V2Lastest {
    @GET("api/topics/latest.json")
    Call<List<V2ExBean>> listLastest();
}
