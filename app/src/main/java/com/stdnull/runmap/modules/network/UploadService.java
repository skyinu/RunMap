package com.stdnull.runmap.modules.network;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadService {
    @Multipart
    @POST("/action/upload.js")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);
}