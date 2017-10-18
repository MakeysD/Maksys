package com.example.duan.chao.DCZ_util;


import com.example.duan.chao.DCZ_application.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttpUtils
 */
public class OkHttpUtils {
    private static OkHttpClient singleton;

    private static final long   OKCLIENT_DISK_CACHE_SIZE = 20 * 1024 * 1024;
    private static final String OKCLIENT_DISK_CACHE_NAME = "http-cache";
    public static OkHttpClient getInstance() {

        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getContext()));
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                            .cookieJar(cookieJar)
                            .readTimeout(20000L, TimeUnit.MILLISECONDS)
                            .writeTimeout(15000L, TimeUnit.MILLISECONDS);
                    builder.addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                            .addInterceptor(new AddCookie())
                            .addInterceptor(new AddUpdate())
                            //.addInterceptor(new AddParamInterceptor())
                            .cache(new Cache(
                                    new File("../", OKCLIENT_DISK_CACHE_NAME),
                                    OKCLIENT_DISK_CACHE_SIZE));
//                    singleton=builder.build();
                    singleton=builder.build();
                }
            }
        }
        return singleton;
    }

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final MediaType type = MediaType.parse("text/html;charset=gb2312");

    static {
        mOkHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 根据url 和 params 获取到服务器数据
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String getStringFromServer(String url, Map<String, String> params) throws IOException {
        Request request = new Request.Builder().url(url).post(requestBody(params)).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * map 转 RequestBody
     * @param params
     * @return
     */
    public static RequestBody requestBody(Map<String, String> params) {
        String content = "";
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            content += entry.getKey() + "=" + entry.getValue();
            if (it.hasNext()) {
                content += "&";
            }
        }
        return RequestBody.create(type, content);
    }
}
