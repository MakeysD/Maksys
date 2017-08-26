package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.StatusBarUtil;

import java.util.Locale;

/**
 * Created by duan on 2017/6/16.
 */

public class BaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImgTransparent(this);      //这行是让标题沉浸
        Log.i("BaseActivity",getClass().getSimpleName());
        ActivityUtils.getInstance().pushActivity(this);
        Log.i("dcz栈的数量",ActivityUtils.getInstance().ActivitySize()+"");
        if(MyApplication.language.equals("CHINESE")){
            Locale.setDefault(Locale.CHINESE);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.CHINESE;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }else if(MyApplication.language.equals("ENGLISH")){
            Locale.setDefault(Locale.ENGLISH);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.ENGLISH;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
