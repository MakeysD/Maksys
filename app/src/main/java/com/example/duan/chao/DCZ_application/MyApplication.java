package com.example.duan.chao.DCZ_application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;

import com.example.duan.chao.DCZ_jiguang.Logger;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by duan on 2017/5/22.
 */

public class MyApplication extends Application{
    public static String uri="http://192.168.2.171:9898/";
    private static final String TAG = "JIGUANG-Example";
    public static boolean zhiwen=false;
    private static Context context;
    public static boolean first=true;//是否为第一次登录
    public static boolean isLogin=false;
    public static Boolean suo=true;     //是否打开手势解锁页面
    public static String qiniu="https://pic.bincrea.com/";
    public static String city="中国";

    public static String device;
    public static String username;
    public static String token;
    public static String xinghao;
    public static String brand;
    //偏好设置
    public static SharedPreferences sf;
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "[MyApplication] onCreate");
        context=getApplicationContext();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        Fresco.initialize(this);
       /* try {
            FileUtilities.restrictAccessToOwnerOnly(
                    getApplicationContext().getApplicationInfo().dataDir);
        } catch (Throwable e) {
        }
        DependencyInjector.configureForProductionIfNotConfigured(getApplicationContext());*/
        sf= PreferenceManager.getDefaultSharedPreferences(this);
        first = sf.getBoolean("first",true);
        zhiwen=sf.getBoolean("zhiwen",false);
        token=sf.getString("token","");
        username=sf.getString("username","");
        city=sf.getString("city","");
        Log.i("dcz_first",first+"");
    }
    public static Context getContext(){
        return context;
    }

   public static float dp2Px() {
       return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
   }
}
