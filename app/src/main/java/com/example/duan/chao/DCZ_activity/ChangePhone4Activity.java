package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;
import android.view.View;
/**
 *  验证密保手机
 *
 * */
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  验证新的密保手机
 *
 * */
public class ChangePhone4Activity extends BaseActivity {
    private ChangePhone4Activity INSTANCE;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone4);
        ButterKnife.bind(this);
        INSTANCE=this;
        setViews();
        setListener();
    }

    /**
     *  数据初始化
     * */
    private void setViews() {

    }
    /**
     * 监听
     *
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
