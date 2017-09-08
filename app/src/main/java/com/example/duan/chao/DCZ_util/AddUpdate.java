package com.example.duan.chao.DCZ_util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_activity.LoginEmailActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HttpBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.google.gson.Gson;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Field;

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
        if(result.getCode()!=null){
          /*  if (result.getCode().equals("10011")){
                throw  new MyThrow();
            }*/
            if(result.getCode().equals("20003")){
                Log.i("ddddddddd","安全中心不可用");
                Request loginRequest = getLoginRequest();
                Response loginResponse = chain.proceed(loginRequest);
                String loginString = loginResponse.body().string();
                HttpBean resultLogin = mGson.fromJson(loginString, HttpBean.class);
                Activity a = ActivityUtils.getInstance().getCurrentActivity();
                if(resultLogin.getCode().equals("10500")){
                    Log.i("dcz","安全中心不可用");
                }else if(resultLogin.getCode().equals("20000")) {
                    return chain.proceed(originalRequest);
                }else {
                    Log.i("dcz刷新token",resultLogin.getCode());
                    MyApplication.sms_type="0";MyApplication.sf.edit().putString("sms_type","0").commit();
                    MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                    //下线通知
                    MainActivity.mHandler.sendEmptyMessage(1);
                }
            }
        }

        originalResponse = originalResponse.newBuilder()
                .body(ResponseBody.create(null, s))
                .build();
        return originalResponse;
    }

    private Request getLoginRequest() {
        return new Request.Builder()//http://110.79.11.5/user-safe-api/loginByRefreshToken
                .url("http://110.79.11.5/user-safe-api/loginByRefreshToken")
                .post(new FormBody.Builder()
                        .add("username", MyApplication.username)
                        .add("refreshToken",MyApplication.token)
                        .add("deviceUUID",MyApplication.device)
                        .build())
                .build();
    }
    private Request Login() {
        return new Request.Builder()
                .url("http://110.79.11.5/user-safe-api/login")
                .post(new FormBody.Builder()
                        .add("username", MyApplication.username)
                        .add("password",MyApplication.password)
                        .add("deviceUUID",MyApplication.device)
                        .add("deviceName",MyApplication.xinghao)
                        .add("registrationId",MyApplication.rid)
                        .build())
                .build();
    }

    public static class MyThrow extends IOException{

    }
}
