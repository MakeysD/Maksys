package com.example.duan.chao;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duan.chao.DCZ_activity.BaseActivity;
import com.example.duan.chao.DCZ_activity.GesturesLockActivity;
import com.example.duan.chao.DCZ_activity.LockActivity;
import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_activity.ScanActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_jiguang.ExampleUtil;
import com.example.duan.chao.DCZ_jiguang.LocalBroadcastManager;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.DragLayout;
import com.example.duan.chao.DCZ_selft.DragRelativeLayout;
import com.example.duan.chao.DCZ_selft.SwitchButton;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity{
    private final String TAG = "MainActivity";
    private TextView result = null;
    private Dialog dialog;

    //下面的是极光需要
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    public static boolean isForeground = false;
    //上面的是极光需要

    private Handler handler = null;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;
    private MainActivity INSTANCE;
    private DragLayout mDragLayout;
    private DraweeController dra;
    private timeThread thread;


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
    @BindView(R.id.button2)
    SwitchButton button2;     //指纹锁

    @BindView(R.id.iv)
    SimpleDraweeView iv;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        ButterKnife.bind(this);
        registerMessageReceiver();
        setViews();
        setListener();
    }
    private class timeThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 2; i > 0; i--) {
                try {
                    Message msg = handler.obtainMessage();
                    msg.what=0;
                    msg.arg1 = i;         //秒数赋值给消息
                    handler.sendMessage(msg);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void newhandler() {
        handler = new Handler(INSTANCE.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    if(msg.arg1==1){
                        dra.getAnimatable().stop();
                        tv.setVisibility(View.VISIBLE);
                    }else {
                        dra= Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true).setUri(Uri.parse("asset://com.example.duan.chao/agif.gif")).build();
                        iv.setController(dra);
                    }
                }
            }
        };
    }
    private void setViews() {
        //setAnimation(R.anim.rotate,iv);
        newhandler();
        thread = null;
        thread = new timeThread();
        thread.start();
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl5).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl6).rippleCorner(MyApplication.dp2Px()).create();
        mDragLayout = (DragLayout) findViewById(R.id.dsl);
        mDragLayout.setDragListener(mDragListener);
        DragRelativeLayout mMainView = (DragRelativeLayout) findViewById(R.id.rl_main);
        mMainView.setDragLayout(mDragLayout);
        if(MyApplication.zhiwen==true){
            button2.setChecked(true);
        }else {
            button2.setChecked(false);
        }
        //初始化指纹.
        fingerprintManager = FingerprintManagerCompat.from(this);
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

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dra.getAnimatable().stop();
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
                //如果手势密码为0
                if(LockUtil.getPwdStatus(INSTANCE)==true){
                    Intent intent=new Intent(INSTANCE, LockActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(INSTANCE, GesturesLockActivity.class);
                    startActivity(intent);
                }

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
                getData();
            }
        });
        //指纹锁的开关
        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    //开启指纹锁
                    startZhiwen();
                } else {
                    //关闭指纹锁
                    MyApplication.sf.edit().putBoolean("zhiwen", false).commit();
                    MyApplication.zhiwen=false;
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
        isForeground = true;
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
    /**
     *   指纹身份验证这里开始
     * */
    private void startZhiwen(){
        //先判断有没有指纹传感器
        if (!fingerprintManager.isHardwareDetected()) {
            // 没有检测到指纹传感器，显示对话框告诉用户
            button2.setChecked(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("没有指纹传感器");
            builder.setMessage("在你的设备上没有指纹传感器，点击取消退出");
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //显示提示框
            builder.create().show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 没有一个指纹图像被登记
            button2.setChecked(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("没有指纹登记");
            builder.setMessage("没有指纹登记,输入\\ n");
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //显示提示框
            builder.create().show();
        } else {
            try {
                myAuthCallback = new MyAuthCallback(handler);
                MyApplication.sf.edit().putBoolean("zhiwen", true).commit();
                MyApplication.zhiwen=true;
                Toast.makeText(INSTANCE, "已开启指纹验证", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().exit_login(MyApplication.device).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(INSTANCE, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(INSTANCE,"失败", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(INSTANCE,"尚未登录设备", Toast.LENGTH_SHORT).show();
                    Log.d("dcz_数据获取失败",response.message());
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                dialog.dismiss();
                Log.i("dcz异常",call.toString());
                Toast.makeText(INSTANCE, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
    private void setAnimation(int id,ImageView iv){
        Animation operatingAnim = AnimationUtils.loadAnimation(INSTANCE, id);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            iv.startAnimation(operatingAnim);
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    Log.i("dcz",showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }
}
