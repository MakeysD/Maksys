package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.AddUpdate;
import com.example.duan.chao.DCZ_util.StatusBarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        //判断APP是否在前台
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(this.getPackageName())) {
           Log.i("dcz","APP已进入前台");
        }else {
           Log.i("dcz","APP已进入后台");
           Log.i("dcz",System.currentTimeMillis()+"");
            MyApplication.start_time=System.currentTimeMillis();
            MyApplication.classname=getClass().getSimpleName();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        //判断APP是否在前台
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(this.getPackageName())) {
            Log.i("dcz2","APP已进入前台");
            if(MyApplication.start_time!=null&& AddUpdate.MyThrow.class!=null){
                if(MyApplication.classname.equals(getClass().getSimpleName())){
                    long time =System.currentTimeMillis()-MyApplication.start_time;
                    Log.i("dcz_后台待机时间",time/1000+"");
                    if(time/1000>300){
                       /* ActivityUtils.getInstance().popAllActivities();
                        Intent inten=new Intent(this, LoginEmailActivity.class);
                        startActivity(inten);*/
                    }
                }
            }
        }else {
            Log.i("dcz2","APP已进入后台");
        }
    }

    /**
     * 日期转换成秒数
     * */
    public static long getSecondsFromDate(String expireDate){
        if(expireDate==null||expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try{
            date=sdf.parse(expireDate);
            return (long)(date.getTime()/1000);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
            return 0L;
        }
    }
}
