package com.example.duan.chao;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duan.chao.DCZ_activity.BaseActivity;
import com.example.duan.chao.DCZ_activity.FingerprinProtectActivity;
import com.example.duan.chao.DCZ_activity.GesturesLockActivity;
import com.example.duan.chao.DCZ_activity.GuanYuActivity;
import com.example.duan.chao.DCZ_activity.LockActivity;
import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_activity.ScanActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.DragLayout;
import com.example.duan.chao.DCZ_selft.DragRelativeLayout;
import com.example.duan.chao.DCZ_selft.SwitchButton;
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
    @BindView(R.id.add)
    LinearLayout add;

    @BindView(R.id.tv_suo)
    TextView tv_suo;
    @BindView(R.id.tv_anquan)
    TextView tv_anquan;
    @BindView(R.id.button1)
    SwitchButton button1;     //退出当前账户
    @BindView(R.id.button2)
    SwitchButton button2;     //指纹锁

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
                quan();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LoginActivity.class);
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
        //手势锁
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LockActivity.class);
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
        //指纹锁
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(INSTANCE, "暂未开启此功能", Toast.LENGTH_SHORT).show();
             /*   Intent intent=new Intent(INSTANCE, FingerprinProtectActivity.class);
                startActivity(intent);*/
            }
        });
        //关于
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(INSTANCE, "暂未开启此功能", Toast.LENGTH_SHORT).show();
              /*  Intent intent=new Intent(INSTANCE, GuanYuActivity.class);
                startActivity(intent);*/
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
        //账户安全的开关
        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    Toast.makeText(INSTANCE, "已开启", Toast.LENGTH_SHORT).show();
                    rl2.setEnabled(true);
                } else {
                    Toast.makeText(INSTANCE, "已关闭", Toast.LENGTH_SHORT).show();
                    rl2.setEnabled(false);
                }
            }
        });
        //指纹锁的开关
        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    Toast.makeText(INSTANCE, "已开启", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(INSTANCE, "已关闭", Toast.LENGTH_SHORT).show();
                }
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
           // ViewHelper.setAlpha(back, 1 - percent);
        }
        @Override
        public void onStartOpen(DragLayout.Direction direction) {
           // Utils.showToast(getApplicationContext(), "onStartOpen: " + direction.toString());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(LockUtil.getPwdStatus(INSTANCE)==true){
            tv_suo.setText("已开启");
        }else {
            tv_suo.setText("未开启");
        }
    }


    //手动开启相机权限
    private void quan(){
        if(ContextCompat.checkSelfPermission(INSTANCE, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent intent=new Intent(INSTANCE, ScanActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限获取成功
                Log.i("dcz","权限获取成功");
                Intent intent=new Intent(INSTANCE, ScanActivity.class);
                startActivity(intent);
            }else{
                //权限被拒绝
                Log.i("dcz","权限被拒绝");
            }
        }
    }
}
