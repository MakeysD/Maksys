package com.example.duan.chao.DCZ_util;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HttpBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by DELL on 2017/7/24.
 */

public class AddUpdate implements Interceptor{
    private Gson mGson = new Gson();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response originalResponse = chain.proceed(originalRequest);
        String s = originalResponse.body().string();
        LoginBean result = mGson.fromJson(s, LoginBean.class);
        if(result.getCode().equals("20003")){
            Request loginRequest = getLoginRequest();
            Response loginResponse = chain.proceed(loginRequest);
            String loginString = loginResponse.body().string();
            HttpBean resultLogin = mGson.fromJson(loginString, HttpBean.class);
            if(resultLogin.getCode().equals("20000")) {
                return chain.proceed(originalRequest);
            }
        }
        originalResponse = originalResponse.newBuilder()
                .body(ResponseBody.create(null, s))
                .build();
        return originalResponse;
    }

    private Request getLoginRequest() {
        return new Request.Builder()
                .url("http://192.168.2.111:8088/user-safe-api/loginByRefreshToken")
                .post(new FormBody.Builder()
                        .add("username", MyApplication.username)
                        .add("refreshToken",MyApplication.token)
                        .add("deviceUUID",MyApplication.device)
                        .build())
                .build();
    }
}
