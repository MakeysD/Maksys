package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.CustomLockView;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_lockdemo.ScreenObserver;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;


/**
 *  手势解锁页面
 *
 * */
public class LoginLockActivity extends BaseActivity {
    private String type;//1 是登录，2是删除锁,3或者null是更改密码
    public Context context;
    public <K extends View> K getViewById(int id) {
        return (K) getWindow().findViewById(id);
    }
    private ScreenObserver mScreenObserver;
    private TextView tvWarn;
    private int[] mIndexs;
    private View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_lock);
        context=this;
        type=getIntent().getStringExtra("type");
        initView();
        if(type.equals("1")){
            tvWarn.setText(this.getString(R.string.tishi26b));
        }else if(type.equals("3")){
            tvWarn.setText(this.getString(R.string.tishi26));
        }else if(type.equals("2")){
            tvWarn.setText(this.getString(R.string.tishi26));
        }
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenStateChange(boolean isScreenOn) {
                if (!isScreenOn&& LockUtil.getPwdStatus(context)&& LockUtil.getPwd(context).length>0) {
                    doSomethingOnScreenOff();
                }
            }
        });
        mIndexs= LockUtil.getPwd(this);
        //判断当前是否设置过密码，没有设置过，直接跳转到设置手势密码页面
        if(LockUtil.getPwdStatus(context)){
            final CustomLockView cl=(CustomLockView)findViewById(R.id.cl);
            cl.setmIndexs(mIndexs);
            cl.setErrorTimes(5);
            cl.setStatus(1);
            cl.setShow(false);
            cl.hui(new CustomLockView.OnType(){
                @Override
                public void ontate() {
                    tvWarn.setText(R.string.tishi26d);
                    tvWarn.setTextColor(getResources().getColor(R.color.red));
                    tvWarn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                }
            });
            cl.setOnCompleteListener(new CustomLockView.OnCompleteListener() {
                @Override
                public void onComplete(int[] indexs) {
                    if(type!=null){
                        //刚进来或关锁进来
                        if(type.equals("1")){
                            //刚进来APP
                            Toast.makeText(LoginLockActivity.this,R.string.tishi73,Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginLockActivity.this, MainActivity.class);
                            startActivity(intent);
                            MyApplication.suo=false;
                        }else if(type.equals("2")){
                            LockUtil.setPwdStatus(context,false);
                            Activity ac = ActivityUtils.getInstance().getActivity(ActivityUtils.getInstance().ActivitySize() - 2);
                            ActivityUtils.getInstance().popActivity(ac);
                            Toast.makeText(LoginLockActivity.this,R.string.lock6,Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent=new Intent(LoginLockActivity.this, GesturesLockActivity.class);
                            startActivity(intent);
                            ActivityUtils.getInstance().popActivity(LoginLockActivity.this);
                        }
                    }else {
                        //修改密码或设置密码进来
                        Toast.makeText(LoginLockActivity.this,context.getString(R.string.tishi73),Toast.LENGTH_SHORT).show();
                    }
                    ActivityUtils.getInstance().popActivity(LoginLockActivity.this);
                }

                @Override
                public void onError() {
                    tvWarn.setText(context.getString(R.string.tishi26d));
                    tvWarn.setTextColor(getResources().getColor(R.color.red));
                    tvWarn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                   /* if (cl.getErrorTimes() > 0) {
                        tvWarn.setText(context.getString(R.string.lock7) + cl.getErrorTimes() +context.getString(R.string.lock8));
                        tvWarn.setTextColor(getResources().getColor(R.color.red));
                    }else {
                        LockUtil.setPwdStatus(context,false);
                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        Intent intent=new Intent(context,LoginEmailActivity.class);
                        startActivity(intent);
                        ActivityUtils.getInstance().popActivity(LoginLockActivity.this);
                        Log.i("dcz","解锁已达到上限");
                    }*/
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        if(mScreenObserver!=null){
            mScreenObserver.stopScreenStateUpdate();
        }
        super.onDestroy();
    }

    /**
     * 打开验证手势
     */
    private void doSomethingOnScreenOff() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), LoginLockActivity.class);
        intent.putExtra("current","resume");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityUtils.getInstance().popActivity(this);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        tvWarn=getViewById(R.id.tvWarn);
        back=getViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(LoginLockActivity.this);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ActivityUtils.getInstance().popActivity(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityUtils.getInstance().popActivity(this);
    }
}
