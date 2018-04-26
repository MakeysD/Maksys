package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation.AnimationListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.example.duan.chao.DCZ_activity.CityListActivity.jsonToList;


/**
 *  APP启动页
 *
 * */
public class AppStartActivity extends BaseActivity {
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private Handler mHandler;
    private ImageView iv;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用LayoutInflater来加载activity_splash.xml视图
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_app_start, null);
        /**
         * 这里不能使用findViewById(R.layout.acitivyt_spash)方法来加载
         * 因为还没有开始调用setContentView()方法，也就是说还没给当前的Activity
         * 设置视图，当前Activity Root View为null，findViewById()方法是从当前
         * Activity的Root View中获取子视图，所以这时候会报NullPointerException异常
         *
         * View rootView = findViewById(R.layout.activity_splash);
         *
         */
        setContentView(rootView);
        quan();
       /* mHandler = new Handler();
        //初始化渐变动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        //开始播放动画
        rootView.startAnimation(animation);
        //设置动画监听器
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当监听到动画结束时，开始跳转到MainActivity中去
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        suo();
                    }
                });
            }
        });*/
    }

    private void suo() {
        //判断是否登录
        if(MyApplication.token.equals("")){
            Intent intent = new Intent(AppStartActivity.this, LoginEmailActivity.class);
            startActivity(intent);
            //判断是否是第一次登录
         /*   if(MyApplication.first){
                Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
                startActivity(intent);
            }*/
        }else {
            //判断是否设置过指纹锁
        /*    if(MyApplication.zhiwen==true){
                Intent intent = new Intent(AppStartActivity.this, ZhiwenActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                //判断当前是否设置过手势锁密码
            }else if(LockUtil.getPwdStatus(this)==true&& MyApplication.suo==true){
                Intent intent=new Intent(this,StartLockActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }else {*/
                Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                startActivity(intent);
         //   }
        }
        ActivityUtils.getInstance().popActivity(this);
    }

    private void quan(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            Log.i("dcz","执行3");
        } else {
            Log.i("dcz","执行2");
            //延迟2S跳转
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    suo();
                }
            }, 700);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i("dcz","执行");
                }else {
                    ActivityUtils.getInstance().popActivity(this);
                }
                break;
            default:
                break;
        }
    }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Log.i("dcz","按下了返回键");
            ActivityUtils.getInstance().popActivity(this);
        }
}
