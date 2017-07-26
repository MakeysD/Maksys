package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *     登录
 *
 * */
public class LoginActivity extends BaseActivity {
    private LoginActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;       //下一步
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        INSTANCE=this;
        ButterKnife.bind(this);
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
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,SmsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
