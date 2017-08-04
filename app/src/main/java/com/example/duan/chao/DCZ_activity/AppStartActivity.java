package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;


/**
 *  APP启动页
 *
 * */
public class AppStartActivity extends Activity {
    private int[] mIndexs;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        quan();
    }

    private void suo() {
        Log.i("dcz_设备ID", ShebeiUtil.getDeviceId(this));
        Log.i("dcz_设备型号",ShebeiUtil.getPhoneModel());
        Log.i("dcz_手机品牌",ShebeiUtil.getPhoneBrand());
        MyApplication.device=ShebeiUtil.getDeviceId(this);
        MyApplication.xinghao=ShebeiUtil.getPhoneModel();
        //判断是否登录
        if(MyApplication.token.equals("")){
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
                if(MyApplication.first){
                    Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }else {
            Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();

        //mIndexs= LockUtil.getPwd(this);
    }

    private void quan(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            Log.i("dcz","执行3");
        } else {
            Log.i("dcz","执行2");
            suo();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i("dcz","执行");
                    suo();
                }else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
