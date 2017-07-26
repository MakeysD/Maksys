package com.example.duan.chao;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.duan.chao.DCZ_activity.BaseActivity;
import com.example.duan.chao.DCZ_activity.FingerprinProtectActivity;
import com.example.duan.chao.DCZ_activity.GesturesLockActivity;
import com.example.duan.chao.DCZ_activity.GuanYuActivity;
import com.example.duan.chao.DCZ_activity.LockActivity;
import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_activity.ScanActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_selft.DragLayout;
import com.example.duan.chao.DCZ_selft.DragRelativeLayout;
import com.nineoldandroids.view.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {
    private MainActivity INSTANCE;
    private DragLayout mDragLayout;
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
    @BindView(R.id.scan)
    ImageView scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
    }


    private void setViews() {
        mDragLayout = (DragLayout) findViewById(R.id.dsl);
        mDragLayout.setDragListener(mDragListener);
        DragRelativeLayout mMainView = (DragRelativeLayout) findViewById(R.id.rl_main);
        mMainView.setDragLayout(mDragLayout);
    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragLayout.open(true);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ScanActivity.class);
                startActivity(intent);
            }
        });
        //安全保护
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, SecurityProtectActivity.class);
                startActivity(intent);
            }
        });
        //账户安全
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ZhangHuSercurityActivity.class);
                startActivity(intent);
            }
        });
        //手势锁
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LockActivity.class);
                startActivity(intent);
            }
        });
        //指纹锁
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, FingerprinProtectActivity.class);
                startActivity(intent);
            }
        });
        //关于
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, GuanYuActivity.class);
                startActivity(intent);
            }
        });
        //退出当前账户
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private DragLayout.OnDragListener mDragListener = new DragLayout.OnDragListener() {
        @Override
        public void onOpen() {
        }
        @Override
        public void onClose() {
           // shakeHeader();
        }
        @Override
        public void onDrag(final float percent) {
            /*主界面左上角头像渐渐消失*/
            ViewHelper.setAlpha(back, 1 - percent);
        }
        @Override
        public void onStartOpen(DragLayout.Direction direction) {
           // Utils.showToast(getApplicationContext(), "onStartOpen: " + direction.toString());
        }
    };
}
