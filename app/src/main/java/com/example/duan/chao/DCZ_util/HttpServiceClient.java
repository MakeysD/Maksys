package com.example.duan.chao.DCZ_util;

import android.content.Context;

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
    public static InterfaceService getInstance(){
        RetrofitUtils.setUrl_ROOT("http://110.79.11.5/user-safe-api/");//http://110.79.11.13:80/user-safe-api/
        interfaceService=RetrofitUtils.createApiForGson(InterfaceService.class);
        return  interfaceService;
    }
}
