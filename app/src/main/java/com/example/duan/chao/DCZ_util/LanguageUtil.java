package com.example.duan.chao.DCZ_util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;

import java.util.Locale;

/**
 * Created by DELL on 2018/6/20.
 */

public class LanguageUtil {
    public static void a(Context context){
        Log.i("语言", MyApplication.language+"语言");
        String yu = context.getResources().getConfiguration().locale.toString();
        Log.i("dcz_系统语言2",yu);
        Log.i("dcz_系统语言3",MyApplication.xitong);
        Log.i("dcz_系统语言4", Locale.getDefault().getLanguage()+Locale.getDefault().getCountry());
        Log.i("dcz_系统语言5",context.getResources().getConfiguration().locale.getCountry()+"z");
        if(yu.equals("en_US")){     //对于多机型，这个为了保存系统语言
            MyApplication.xitong="en_US";MyApplication.sf.edit().putString("xitong","en_US").commit();
        }else if(yu.equals("en_GB")){
            MyApplication.xitong="en_GB";MyApplication.sf.edit().putString("xitong","en_GB").commit();
        }else if(yu.equals("th_TH")){
            MyApplication.xitong="th_TH";MyApplication.sf.edit().putString("xitong","th_TH").commit();
        }else if(yu.equals("zh_CN")){
            MyApplication.xitong="zh_CN";MyApplication.sf.edit().putString("xitong","zh_CN").commit();
        }else if(yu.equals("sk_SK")){
            MyApplication.xitong="sk_SK";MyApplication.sf.edit().putString("xitong","sk_SK").commit();
        }else if(yu.equals("vi_rVN")||yu.equals("vi_VN")){
            MyApplication.xitong="vi_VN";MyApplication.sf.edit().putString("xitong","vi_VN").commit();
        }else if(yu.equals("zh_TW")){
            MyApplication.xitong="zh_TW";MyApplication.sf.edit().putString("xitong","zh_TW").commit();
        }else if(yu.equals("zh_CN")){
            MyApplication.xitong="zh_CN";MyApplication.sf.edit().putString("xitong","zh_CN").commit();
        }
        if(MyApplication.language.equals("")){
            if(MyApplication.xitong.equals("en_US")||MyApplication.xitong.equals("en_GB")){
                Locale locale = new Locale("en");
                Configuration config = context.getResources().getConfiguration();
                config.locale =locale;
                context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            }else if(MyApplication.xitong.equals("th_TH")){
                Locale locale = new Locale("th");
                Configuration config = context.getResources().getConfiguration();
                config.locale =locale;
                context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            }else if(MyApplication.xitong.equals("sk")){
                Locale locale = new Locale("sk");
                Configuration config = context.getResources().getConfiguration();
                config.locale =locale;
                context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            }else if(MyApplication.xitong.equals("vi")){
                Locale locale = new Locale("vi");
                Configuration config = context.getResources().getConfiguration();
                config.locale =locale;
                context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            }else {
                Locale locale = new Locale("zh");
                Configuration config = context.getResources().getConfiguration();
                config.locale =locale;
                context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            }
            return;
        }
        if(MyApplication.language.equals("CHINESE")){
            Locale locale = new Locale("zh");
            Configuration config = context.getResources().getConfiguration();
            config.locale =locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }else if(MyApplication.language.equals("ENGLISH")){
            /*Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.ENGLISH;*/
            Locale locale = new Locale("en");
            Configuration config =context.getResources().getConfiguration();
            config.locale =locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }else if(MyApplication.language.equals("CHINESE_TW")){
            Configuration config = context.getResources().getConfiguration();
            config.locale = Locale.TRADITIONAL_CHINESE;//将语言更改为繁体中文
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            /*Locale locale = new Locale("zh_rtw");
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale =locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
        }else if(MyApplication.language.equals("SK")){
            Locale locale = new Locale("sk");
            Configuration config = context.getResources().getConfiguration();
            config.locale =locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }else if(MyApplication.language.equals("VI")){
            Locale locale = new Locale("vi");
            Configuration config = context.getResources().getConfiguration();
            config.locale =locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }else {
            Locale locale = new Locale("th");
            Configuration config = context.getResources().getConfiguration();
            config.locale =locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }
    }
}
