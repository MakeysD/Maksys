package com.example.duan.duan.DCZ_util;

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

        RetrofitUtils.setUrl_ROOT("http://api.yueyetv.com/a1/");
        interfaceService=RetrofitUtils.createApiForGson(InterfaceService.class);
        return  interfaceService;
    }
}
