package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.AddUpdate;
import com.example.duan.chao.DCZ_util.CodeUtil;
import com.example.duan.chao.DCZ_util.StatusBarUtil;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by duan on 2017/6/16.
 */

public class BaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImgTransparent(this);      //这行是让标题沉浸
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        Log.i("BaseActivity",getClass().getSimpleName());
        Log.i("dcz_type",MyApplication.type+"");
        if(MyApplication.type==0){
            ActivityUtils.getInstance().pushActivity(this);
        }else {
            MyApplication.type=0;
        }
        Log.i("dcz栈的数量",ActivityUtils.getInstance().ActivitySize()+"");
        for(int i=0;i<ActivityUtils.getInstance().ActivitySize();i++){
            Log.i("dcz_栈",ActivityUtils.getInstance().getActivity(i)+"");
        }
        Log.i("语言",MyApplication.language+"语言");
        String yu = getBaseContext().getResources().getConfiguration().locale.toString();
        Log.i("dcz_系统语言2",yu);
        Log.i("dcz_系统语言3",MyApplication.xitong);
        if(yu.equals("en_US")){     //对于多机型，这个为了保存系统语言
            MyApplication.xitong="en_US";MyApplication.sf.edit().putString("xitong","en_US").commit();
        }else if(yu.equals("en_GB")){
            MyApplication.xitong="en_GB";MyApplication.sf.edit().putString("xitong","en_GB").commit();
        }else if(yu.equals("th_TH")){
            MyApplication.xitong="th_TH";MyApplication.sf.edit().putString("xitong","th_TH").commit();
        }else if(yu.equals("zh_CN")){
            MyApplication.xitong="zh_CN";MyApplication.sf.edit().putString("xitong","zh_CN").commit();
        }
        if(MyApplication.language.equals("")){
            if(MyApplication.xitong.equals("en_US")||MyApplication.xitong.equals("en_GB")){
                Locale locale = new Locale("en");
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale =locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }else if(MyApplication.xitong.equals("th_TH")){
                Locale locale = new Locale("th");
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale =locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }else {
                Locale locale = new Locale("zh");
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale =locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }
            CodeUtil.pushcode(getApplicationContext());
            return;
        }
        if(MyApplication.language.equals("CHINESE")){
            Locale locale = new Locale("zh");
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale =locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }else if(MyApplication.language.equals("ENGLISH")){
            /*Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.ENGLISH;*/
            Locale locale = new Locale("en");
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale =locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }else {
            Locale locale = new Locale("th");
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale =locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        CodeUtil.pushcode(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        //判断APP是否在前台
        if(ActivityUtils.getInstance().isAppOnForeground(this)==false) {
            Log.i("dcz","APP已进入后台");
            Log.i("dcz",System.currentTimeMillis()+"");
            MyApplication.AppOnForeground=true;
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
        if(ActivityUtils.getInstance().getCurrentActivity()instanceof LoginEmailActivity||ActivityUtils.getInstance().getCurrentActivity()instanceof SmsActivity){
        }else {
            JPushInterface.onResume(this);
        }
       /* if(getClass().getSimpleName().contains("StartLock")||getClass().getSimpleName().contains("Zhiwen")){
            Log.i("dcz","当前是解锁页面");
        }else {
            if(MyApplication.AppOnForeground==true){
                MyApplication.AppOnForeground=false;
                //判断APP是否在前台
                if(ActivityUtils.getInstance().isAppOnForeground(this)==true) {
                    Log.i("dcz2","APP已进入前台");
                    if(ActivityUtils.getInstance().getCurrentActivity()instanceof MainActivity){
                        MainActivity.mHandler.sendEmptyMessage(2);
                    }
                    if(MyApplication.start_time!=null&& AddUpdate.MyThrow.class!=null){
                        if(MyApplication.classname.equals(getClass().getSimpleName())){
                            long time =System.currentTimeMillis()-MyApplication.start_time;
                            Log.i("dcz_后台待机时间",time/1000+"");
                            if(time/1000>300){
                                //判断是否设置过指纹锁
                                if(MyApplication.zhiwen==true){
                                    Intent intent = new Intent(this, ZhiwenActivity.class);
                                    intent.putExtra("type","1");
                                    startActivity(intent);
                                    //判断当前是否设置过手势锁密码
                                }else if(LockUtil.getPwdStatus(this)==true&& MyApplication.suo==true){
                                    Intent intent=new Intent(this,StartLockActivity.class);
                                    intent.putExtra("type","1");
                                    startActivity(intent);
                                }else {

                                }
                            }
                        }
                    }
                }else {
                    Log.i("dcz2","APP已进入后台");
                }
            }
        }*/

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
