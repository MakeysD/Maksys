package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
/**
 *  验证密保手机
 *
 * */
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
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
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.button)
    TextView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone4);
        ButterKnife.bind(this);
        INSTANCE=this;
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        setViews();
        setListener();
    }

    /**
     *  数据初始化
     * */
    private void setViews() {
        tv.setText(this.getString(R.string.tishi69)+"********8723");
    }
    /**
     * 监听
     *
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
