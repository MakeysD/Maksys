package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  修改密码
 *
 * */
public class ChangePasswordActivity extends BaseActivity {
    private ChangePasswordActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button1)
    TextView button1;           //修改登录密码
    @BindView(R.id.button2)
    TextView button2;           //修改支付密码
    @BindView(R.id.button3)
    TextView button3;           //重置支付密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(button2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(button3).rippleCorner(MyApplication.dp2Px()).create();
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {

    }
    /**
     *  监听
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        //修改登录密码
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ChangeLoginPasswordActivity.class);
                startActivity(intent);
            }
        });
        //修改支付密码
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ChangePayPasswordActivity.class);
                startActivity(intent);
            }
        });
        //重置支付密码
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LookPayPassword2Activity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }
}
