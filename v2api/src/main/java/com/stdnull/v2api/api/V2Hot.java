package com.stdnull.v2api.api;

import com.stdnull.v2api.mode.V2ExBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chen on 2017/8/6.
 */

public interface V2Hot {
    @GET("api/topics/hot.json")
    Call<List<V2ExBean>> listHost();
}
