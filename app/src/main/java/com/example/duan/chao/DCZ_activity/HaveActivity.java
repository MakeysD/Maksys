package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;
import android.view.View;

import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HaveActivity extends BaseActivity {
    private HaveActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have);
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
