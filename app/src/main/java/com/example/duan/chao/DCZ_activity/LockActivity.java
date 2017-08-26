package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      手势锁控制页面
 *
 * */
public class LockActivity extends BaseActivity {
    private LockActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button1)
    View button1;
    @BindView(R.id.button2)
    View button2;
    @BindView(R.id.type)
    TextView type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        INSTANCE=this;
        CanRippleLayout.Builder.on(button1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(button2).rippleCorner(MyApplication.dp2Px()).create();
        setViews();
        setListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }

    private void setViews() {

    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, GesturesLockActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LockUtil.getPwdStatus(INSTANCE)==false){
                    Toast.makeText(INSTANCE,R.string.lock3, Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(INSTANCE, LoginLockActivity.class);
                    intent.putExtra("type","2");
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(LockUtil.getPwdStatus(INSTANCE)==true){
            type.setText(R.string.lock4);
        }else {
            type.setText(R.string.lock5);
        }
    }
}
