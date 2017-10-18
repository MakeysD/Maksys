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
    public static InterfaceService getInstance(){//http://192.168.2.111:8088/user-safe-api
        RetrofitUtils.setUrl_ROOT("http://api.qeveworld.com/user-safe-api/");//http://api.qeveworld.com/user-safe-api/
        interfaceService=RetrofitUtils.createApiForGson(InterfaceService.class);
        return  interfaceService;
    }
}
