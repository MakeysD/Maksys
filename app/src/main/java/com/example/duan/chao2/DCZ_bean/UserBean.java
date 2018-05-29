package com.example.duan.chao2.DCZ_bean;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by DELL on 2017/8/3.
 */

public class UserBean implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request sss = chain.request();
        String ss = sss.urlString();
      //  return chain.proceed(sss);
        return null;
    }
}
