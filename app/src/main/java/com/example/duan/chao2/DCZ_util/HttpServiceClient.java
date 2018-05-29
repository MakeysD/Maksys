package com.example.duan.chao2.DCZ_util;

import android.content.Context;
import android.util.Log;

import com.example.duan.chao2.DCZ_application.MyApplication;

/**
 * Retrofit
 *
 * 网络请求初始化
 */
public class HttpServiceClient {

    private static InterfaceService interfaceService;
    private Context context;
    /**
     * 获取实例
     * @return
     */
    public static InterfaceService getInstance(){//http://192.168.2.111:8088/user-safe-api
        Log.i("dcz",MyApplication.uri);
        RetrofitUtils.setUrl_ROOT(MyApplication.uri);
        interfaceService=RetrofitUtils.createApiForGson(InterfaceService.class);
        return  interfaceService;
    }
}
