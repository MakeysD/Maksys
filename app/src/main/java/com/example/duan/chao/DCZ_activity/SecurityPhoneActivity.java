package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      安全手机
 *
 * */
public class SecurityPhoneActivity extends BaseActivity {
    private SecurityPhoneActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;        //更换
    @BindView(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_phone);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        tv.setText(this.getString(R.string.tishi78)+"132****8723");
    }
    /**
     *  监听
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //更换
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ChangePhone1Activity.class);
                startActivity(intent);
            }
        });
    }
}
