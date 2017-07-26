package com.example.duan.chao.DCZ_application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by duan on 2017/5/22.
 */

public class MyApplication extends Application{
    private static Context context;
    public static boolean first=true;//是否为第一次登录
    public static boolean isLogin=false;
    public static Boolean suo=true;
    public static String qiniu="https://pic.bincrea.com/";
    //偏好设置
    public static SharedPreferences sf;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Fresco.initialize(this);
       /* try {
            FileUtilities.restrictAccessToOwnerOnly(
                    getApplicationContext().getApplicationInfo().dataDir);
        } catch (Throwable e) {
        }
        DependencyInjector.configureForProductionIfNotConfigured(getApplicationContext());*/
        sf= PreferenceManager.getDefaultSharedPreferences(this);
        first = sf.getBoolean("first",true);
        Log.i("dcz_first",first+"");
    }
    public static Context getContext(){
        return context;
    }
}
