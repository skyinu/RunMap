package com.stdnull.runmap.modules.network;

import com.stdnull.baselib.common.CFLog;
import com.stdnull.runmap.common.AppConfig;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by chen on 2017/7/16.
 */

public class NetworkService {
    public static void uploadFile(String path){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getDbBackupBaseUrl())
                .build();
        UploadService service =
                retrofit.create(UploadService.class);
        File file = new File(path);
        if(!file.exists()){
            return;
        }
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("resource", file.getName(), requestFile);
        Call<ResponseBody> call = service.upload(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                CFLog.e("NetworkService", "upload success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CFLog.e("NetworkService", "send error message is " + t.getMessage());
            }
        });
    }
}
