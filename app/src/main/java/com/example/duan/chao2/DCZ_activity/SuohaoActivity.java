package com.example.duan.chao2.DCZ_activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.duan.chao2.DCZ_util.ActivityUtils;
import com.example.duan.chao2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      一键锁号
 *
 * */
public class SuohaoActivity extends BaseActivity {
    private SuohaoActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suohao);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
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
    }
}
