package com.stdnull.v2api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chen on 2017/8/6.
 */

public interface V2Ex {
    @GET("api/topics/hot.json")
    Call<ResponseBody> listHost();
}
