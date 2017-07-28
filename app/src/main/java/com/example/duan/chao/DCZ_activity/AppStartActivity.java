package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
  //      if(MyApplication.isLogin==true){
            //判断当前是否设置过密码
            if(LockUtil.getPwdStatus(this)==true&& MyApplication.suo==true){
                Intent intent=new Intent(this,LoginLockActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }else {
               /* if(MyApplication.first){
                    Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                    startActivity(intent);
                }*/
                Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
 /*       }else {
            Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
            startActivity(intent);
        }*/
        finish();

        //mIndexs= LockUtil.getPwd(this);
    }
}
