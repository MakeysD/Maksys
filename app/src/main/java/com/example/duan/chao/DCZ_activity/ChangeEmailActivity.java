package com.example.duan.chao.DCZ_activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeEmailActivity extends BaseActivity {
    private ChangeEmailActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
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
        tv.setText(this.getString(R.string.tishi66)+"857999639@qq.com");
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

            }
        });
    }
}
