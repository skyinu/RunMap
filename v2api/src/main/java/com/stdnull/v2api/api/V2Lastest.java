package com.stdnull.v2api.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chen on 2017/8/20.
 */

public interface V2Lastest {
    @GET("api/topics/latest.json")
    Call<ResponseBody> listLastest();
}
