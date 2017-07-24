package com.example.duan.chao.DCZ_application;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by duan on 2017/5/22.
 */

public class MyApplication extends Application{
    private static Context context;
    public static String qiniu="https://pic.bincrea.com/";
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Fresco.initialize(this);
    }
    public static Context getContext(){
        return context;
    }
}
