package com.example.duan.chao.DCZ_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.CustomLockView;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_lockdemo.ScreenObserver;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartLockActivity extends BaseActivity {
    private StartLockActivity INSTANCE;
    public Context context;
    public <K extends View> K getViewById(int id) {
        return (K) getWindow().findViewById(id);
    }
    private ScreenObserver mScreenObserver;
    private TextView tvWarn;
    private int[] mIndexs;
    @BindView(R.id.change)
    TextView change;        //去进行指纹解锁
    @BindView(R.id.wangji)
    TextView wangji;        //忘记密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_lock);
        INSTANCE=this;
        ButterKnife.bind(this);
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenStateChange(boolean isScreenOn) {
                if (!isScreenOn&& LockUtil.getPwdStatus(INSTANCE)&& LockUtil.getPwd(INSTANCE).length>0) {
                    doSomethingOnScreenOff();
                }
            }
        });
        initView();
        setListener();
        mIndexs= LockUtil.getPwd(this);
        //判断当前是否设置过密码，没有设置过，直接跳转到设置手势密码页面
        if(mIndexs.length>1){
            final CustomLockView cl=(CustomLockView)findViewById(R.id.cl);
            cl.setmIndexs(mIndexs);
            cl.setErrorTimes(4);
            cl.setStatus(1);
            cl.setShow(false);
            cl.setOnCompleteListener(new CustomLockView.OnCompleteListener() {
                @Override
                public void onComplete(int[] indexs) {
                    //修改密码或设置密码进来
                    Toast.makeText(INSTANCE,"输入正确",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(INSTANCE, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onError() {
                    if (cl.getErrorTimes() > 0) {
                        tvWarn.setText("密码错误，还可以再输入" + cl.getErrorTimes() + "次");
                        tvWarn.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            });
        }
    }


    private void setListener() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,ZhiwenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        wangji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        finish();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        tvWarn=getViewById(R.id.tvWarn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
