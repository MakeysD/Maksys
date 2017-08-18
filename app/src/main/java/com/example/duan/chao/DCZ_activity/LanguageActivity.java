package com.example.duan.chao.DCZ_activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 语言切换
 * */
public class LanguageActivity extends BaseActivity {
    private LanguageActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.rl1)     //简体中文
    RelativeLayout rl1;
    @BindView(R.id.rl2)     //繁体中文
    RelativeLayout rl2;
    @BindView(R.id.rl3)     //英语
    RelativeLayout rl3;
    @BindView(R.id.rl4)     //泰语
    RelativeLayout rl4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl4).rippleCorner(MyApplication.dp2Px()).create();
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
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         /*       MyApplication.language="CHINESE";MyApplication.sf.edit().putString("language","CHINESE").commit();
                recreate();
                MyApplication.status=true;*/
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   MyApplication.language="ENGLISH";MyApplication.sf.edit().putString("language","ENGLISH").commit();
                recreate();//刷新页面
                MyApplication.status=true;*/
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
