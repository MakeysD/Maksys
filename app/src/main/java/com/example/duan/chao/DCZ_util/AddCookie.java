package com.example.duan.chao.DCZ_util;

import com.example.duan.chao.DCZ_bean.LoginBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by DELL on 2017/7/24.
 */

public class AddCookie implements Interceptor{
    private Gson mGson = new Gson();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response originalResponse = chain.proceed(originalRequest);
        String s = originalResponse.body().string();
        LoginBean result = mGson.fromJson(s, LoginBean.class);
        if(result.getCode()!=null){

        }
        originalResponse = originalResponse.newBuilder()
                .body(ResponseBody.create(null, s))
                .build();
        return originalResponse;
    }
}
