package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *     账户安全
 *
 * */

public class ZhangHuSercurityActivity extends BaseActivity {
    private ZhangHuSercurityActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.rl5)
    RelativeLayout rl5;
    @BindView(R.id.rl6)
    RelativeLayout rl6;
    @BindView(R.id.rl7)
    RelativeLayout rl7;
    @BindView(R.id.rl8)
    RelativeLayout rl8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhang_hu_sercurity);
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
        //修改密码
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        //安全手机
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,SecurityPhoneActivity.class);
                startActivity(intent);
            }
        });
        //安全邮箱
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(INSTANCE, "暂未开启此功能", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(INSTANCE,SecurityEmailActivity.class);
                startActivity(intent);
            }
        });
        //实名信息
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(INSTANCE, "暂未开启此功能", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(INSTANCE,PersonDataActivity.class);
                startActivity(intent);
            }
        });
        //一键锁号
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(INSTANCE,SuohaoActivity.class);
                startActivity(intent);*/
            }
        });
        //设备管理
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,EquipmentManageActivity.class);
                startActivity(intent);
            }
        });
        //操作记录
        rl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,OperationRecordActivity.class);
                startActivity(intent);
            }
        });
        //账户足迹
        rl8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,FootprintsActivity.class);
                startActivity(intent);
            }
        });
    }
}
