package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;
import android.view.View;

import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *     安全保护
 *
 * */
public class SecurityProtectActivity extends BaseActivity {
    private SecurityProtectActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_protect);
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
    }
}
