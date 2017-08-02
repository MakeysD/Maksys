package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;


/**
 *  APP启动页
 *
 * */
public class AppStartActivity extends Activity {
    private int[] mIndexs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        suo();
    }

    private void suo() {
    //    if(MyApplication.isLogin==true){
            //判断是否设置过指纹锁
            if(MyApplication.zhiwen==true){
                Intent intent = new Intent(AppStartActivity.this, ZhiwenActivity.class);
                startActivity(intent);
                //判断当前是否设置过手势锁密码
            }else if(LockUtil.getPwdStatus(this)==true&& MyApplication.suo==true){
                Intent intent=new Intent(this,LoginLockActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }else {
                //判断是否是第一次登录
            /*    if(MyApplication.first){
                    Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                    startActivity(intent);
                }*/
                Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                startActivity(intent);
            }
    /*    }else {
            Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
            startActivity(intent);
        }*/
        finish();

        //mIndexs= LockUtil.getPwd(this);
    }
}
