package com.example.duan.duan.DCZ_util;


import com.example.duan.duan.DCZ_bean.StatusBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InterfaceService {
    @GET("https://api.bincrea.com/app/free/getMultimediaList.do")
    Call<StatusBean> getpicpage();

    @Headers({"Content-Type: application/json","Accept: application/json"})//json数据需要添加头
    @POST("https://api.bincrea.com/app/entrust/addEntrustInfo.do")
    Call<StatusBean> set_json(@Body RequestBody body);

    @FormUrlEncoded
    @POST("sms/code")
    Call<StatusBean> jiekou(@Field("mobile") String string);

}
