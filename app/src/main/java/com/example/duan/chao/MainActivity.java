package com.example.duan.chao;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
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
import com.example.duan.chao.DCZ_activity.GuanYuActivity;
import com.example.duan.chao.DCZ_activity.LanguageActivity;
import com.example.duan.chao.DCZ_activity.LockActivity;
import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_activity.ScanActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_jiguang.ExampleUtil;
import com.example.duan.chao.DCZ_jiguang.LocalBroadcastManager;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.DragLayout;
import com.example.duan.chao.DCZ_selft.DragRelativeLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.SwitchButton;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity{
    private Dialog dialog;
    private LoginOkBean data;
    private MediaPlayer player;

    //下面的是极光需要
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CONTENT_TYPE = "content_type";
    public static final String KEY_EXTRAS = "extras";
    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    public static boolean isForeground = true;
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
    @BindView(R.id.rl7)
    RelativeLayout rl7;
    @BindView(R.id.scan)
    ImageView scan;

    @BindView(R.id.tv_suo)
    TextView tv_suo;
    @BindView(R.id.tv_anquan)
    TextView tv_anquan;
    @BindView(R.id.button2)
    SwitchButton button2;     //指纹锁

    @BindView(R.id.iv)
    SimpleDraweeView iv;
   /* @BindView(R.id.tv)
    TextView tv;*/
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.zhanghao)
    TextView zhanghao;
    @BindView(R.id.add)     //添加账号
    LinearLayout add;
    @BindView(R.id.iv1)
    SimpleDraweeView iv1;
    @BindView(R.id.iv2)
    SimpleDraweeView iv2;

    @BindView(R.id.shuaxin)
    ImageView shuaxin;
    @BindView(R.id.bangzhu)
    ImageView bangzhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl5).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl6).rippleCorner(MyApplication.dp2Px()).create();
        registerMessageReceiver();
        setViews();
        setListener();
        MyApplication.status=false;
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
                    Thread.sleep(4000);
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
                        if(dra.getAnimatable()==null){
                            return;
                        }
                        dra.getAnimatable().stop();
                      //  tv.setVisibility(View.VISIBLE);
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
     //   tv.setVisibility(View.GONE);
        newhandler();
        Mp3();
        SharedPreferences sf2 = getSharedPreferences("user2",MODE_PRIVATE);
        final String token = sf2.getString("token","");//第二个参数为默认值
        final String username = sf2.getString("username","");//第二个参数为默认值
        final String mima = sf2.getString("mima","");//第二个参数为默认值
        if(token==null||token.equals("")){
            Log.i("dcz","只有一个账号");
            iv2.setVisibility(View.GONE);
            //add.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        }else {
            Log.i("dcz","有两个账号");
            iv2.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        }
        AssetManager assetManager = this.getAssets();
        try {
            AssetFileDescriptor afd = assetManager.openFd("mp3.mp3");
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            // player.setLooping(true);//循环播放
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dra.getAnimatable()==null){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi84),R.style.registDialog).show();
                }else if(dra.getAnimatable().isRunning()){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi84),R.style.registDialog).show();
                }else {
                    login(username,mima);
                }
            }
        });
        name.setText(MyApplication.nickname);
        zhanghao.setText(MyApplication.username);
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
               // mDragLayout.open(true);
                mDragLayout.open(true, DragLayout.Direction.Right);
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
            }
        });
        shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Mp3();
                recreate();
            }
        });
        bangzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(INSTANCE, "开发中...", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(INSTANCE, "暂未开启此功能", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(INSTANCE, GuanYuActivity.class);
                startActivity(intent);
            }
        });
        //退出当前账户
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        //语言
        rl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LanguageActivity.class);
                startActivity(intent);
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
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.main11),R.style.registDialog).show();
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
            tv_suo.setText(R.string.main2);
        }else {
            tv_suo.setText(R.string.main3);
        }
        if(MyApplication.status==true){
            recreate();
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
            new MiddleDialog(INSTANCE,this.getString(R.string.no_sensor_dialog_title),R.style.registDialog).show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 没有一个指纹图像被登记
            button2.setChecked(false);
            new MiddleDialog(INSTANCE,this.getString(R.string.no_fingerprint_enrolled_dialog_title),R.style.registDialog).show();
        } else {
            try {
                myAuthCallback = new MyAuthCallback(handler);
                MyApplication.sf.edit().putBoolean("zhiwen", true).commit();
                MyApplication.zhiwen=true;
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.lock11),R.style.registDialog).show();
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
                        SharedPreferences sf2 = getSharedPreferences("user2",MODE_PRIVATE);
                        final String username = sf2.getString("username","");//第二个参数为默认值
                        final String token = sf2.getString("token","");
                        final String nickname = sf2.getString("nickname","");
                        if(token==null||token.equals("")){
                            Log.i("dcz","只有一个账号");
                            MyApplication.token=token;MyApplication.sf.edit().putString("token","").commit();
                            Intent intent=new Intent(INSTANCE, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            sf2.edit().putString("token","").commit();
                            Log.i("dcz","有两个账号");
                            MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                            MyApplication.nickname=nickname;MyApplication.sf.edit().putString("nickname",nickname).commit();
                            MyApplication.username=username;MyApplication.sf.edit().putString("username",username).commit();
                            setViews();
                            setListener();
                        }
                    } else {
                        new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                    }
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                    Log.d("dcz_数据获取失败",response.message());
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                dialog.dismiss();
                Log.i("dcz异常",call.toString());
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }

    /***
     *  登录
     * */
    public void login(String a,String b){
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().login(a,b,null,MyApplication.private_key,MyApplication.device,MyApplication.xinghao,MyApplication.rid).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
                        new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                        data=response.body().getData();
                        MyApplication.first=false;MyApplication.sf.edit().putBoolean("first",false).commit();
                        if(MyApplication.token!=null&&!(MyApplication.token.equals(""))){
                            SharedPreferences sf2 = INSTANCE.getSharedPreferences("user2",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf2.edit();
                            editor.putString("token",MyApplication.token);
                            editor.putString("nickname",MyApplication.nickname);
                            editor.putString("username",MyApplication.username);
                            editor.commit();
                        }
                        MyApplication.token=data.getRefreshToken();MyApplication.sf.edit().putString("token",data.getRefreshToken()).commit();
                        MyApplication.nickname=data.getNickname();MyApplication.sf.edit().putString("nickname",data.getNickname()).commit();
                        MyApplication.username=data.getUsername();MyApplication.sf.edit().putString("username",data.getUsername()).commit();
                        setViews();
                        setListener();
                    }else {
                        new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                dialog.dismiss();
                Log.i("dcz异常",call.toString());
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
    @Override
    protected void onPause() {
        isForeground = true;
        super.onPause();
    }

    private void Mp3(){
       // tv.setVisibility(View.GONE);
        thread = null;
        thread = new timeThread();
        thread.start();
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                player.start();
            }
        };
        timer.schedule(task,200);
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
                Log.i("dcz","接收到广播");
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    String type = intent.getStringExtra(KEY_CONTENT_TYPE);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    Log.i("dcz",messge);
                    Gson mGson = new Gson();
                    HaveBean result = mGson.fromJson(messge, HaveBean.class);
                    MyApplication.reqFlowId=result.getReqFlowId();
                    MyApplication.reqSysId=result.getReqSysId();
                    Log.i("dcz",result.getReqSysId());
                    Log.i("dcz",type+"type");
                   /* MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
                    MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                    MyApplication.nickname="";MyApplication.sf.edit().putString("nickname","").commit();
                    MyApplication.username="";MyApplication.sf.edit().putString("username","").commit();
                    ActivityUtils.getInstance().popAllActivities();
                    Intent inten=new Intent(INSTANCE, LoginActivity.class);
                    startActivity(inten);*/
                    if(type.equals("2")){//下线通知
                        new MiddleDialog( ActivityUtils.getInstance().getActivity(ActivityUtils.getInstance().ActivitySize()-1), "提示", "您的账号已在另一台设备登录","如果不是本人操作",new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int position) {
                                MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
                                MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                                MyApplication.nickname="";MyApplication.sf.edit().putString("nickname","").commit();
                                MyApplication.username="";MyApplication.sf.edit().putString("username","").commit();
                                ActivityUtils.getInstance().popAllActivities();
                                Intent inten=new Intent(INSTANCE, LoginActivity.class);
                                startActivity(inten);
                            }
                        }, R.style.registDialog).show();
                    }
                }
            } catch (Exception e){
            }
        }
    }
}
