package com.example.duan.chao;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.KeyEvent;
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


import com.chiclam.android.updater.Updater;
import com.chiclam.android.updater.UpdaterConfig;
import com.example.duan.chao.DCZ_activity.BaseActivity;
import com.example.duan.chao.DCZ_activity.GesturesLockActivity;
import com.example.duan.chao.DCZ_activity.GuanYuActivity;
import com.example.duan.chao.DCZ_activity.LanguageActivity;
import com.example.duan.chao.DCZ_activity.LockActivity;
import com.example.duan.chao.DCZ_activity.LoginEmailActivity;
import com.example.duan.chao.DCZ_activity.ScanActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_bean.VersionBean;
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
import com.example.duan.chao.DCZ_util.NotificationsUtils;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.DCZ_zhiwen.CryptoObjectHelper;
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
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity{
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private Dialog dialog;
    private LoginOkBean data;
    private MediaPlayer player;
    private MiddleDialog dia;

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
    private Boolean boo=true;
    private String version;
    private String path="";

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
    @BindView(R.id.tv)
    TextView tv;
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

    @BindView(R.id.language)
    TextView language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        Log.i("dcz类名",INSTANCE.getLocalClassName());
        ButterKnife.bind(this);
        JPushInterface.resumePush(getApplicationContext());
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl5).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl6).rippleCorner(MyApplication.dp2Px()).create();
        registerMessageReceiver();
        setViews();
        getVersion();//版本更新
        setListener();
        initHandler();
        MyApplication.status=false;
    }

    public static Handler mHandler ;
    private void initHandler(){
        //下线通知
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.tishi115),"",new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int position) {
                                ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                ActivityUtils.getInstance().popAllActivities();
                            }
                        }, R.style.registDialog).show();
                        break;
                }
            }
        };
    }
    private void setViews() {
        // 获取packagemanager的实例  
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息  
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packInfo.versionName;
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
        if(MyApplication.language.equals("ENGLISH")){
            iv.setImageResource(R.drawable.progress2);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
            Mp3();
            animationDrawable.start();
            language.setText(R.string.tishi37);
        }else {
            iv.setImageResource(R.drawable.progress);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
            Mp3();
            animationDrawable.start();
            language.setText(R.string.tishi35);
        }

     /*   SharedPreferences sf2 = getSharedPreferences("user2",MODE_PRIVATE);
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
        });*/
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
    }

    private void setdialog(){
        if(NotificationsUtils.isNotificationEnabled(INSTANCE)==false){
            new MiddleDialog(INSTANCE,this.getString(R.string.tishi114),this.getString(R.string.tishi113),new MiddleDialog.onButtonCLickListener2() {
                @Override
                public void onActivieButtonClick(Object bean, int po) {
                    if(bean==null){
                    }else {
                        NotificationsUtils.StartSetting(INSTANCE);
                    }
                }
            }, R.style.registDialog).show();
        }
    }
    private void setCamera(){
        if(NotificationsUtils.isNotificationEnabled(INSTANCE)==false){
            new MiddleDialog(INSTANCE,this.getString(R.string.tishi114),this.getString(R.string.tishi113),new MiddleDialog.onButtonCLickListener2() {
                @Override
                public void onActivieButtonClick(Object bean, int po) {
                    if(bean==null){
                    }else {
                        NotificationsUtils.StartSetting(INSTANCE);
                    }
                }
            }, R.style.registDialog).show();
        }
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
                if(NotificationsUtils.isNotificationEnabled(INSTANCE)==false){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi114),INSTANCE.getString(R.string.tishi113),new MiddleDialog.onButtonCLickListener2() {
                        @Override
                        public void onActivieButtonClick(Object bean, int po) {
                            if(bean==null){
                            }else {
                                NotificationsUtils.StartSetting(INSTANCE);
                            }
                        }
                    }, R.style.registDialog).show();
                }else {
                    quan();
                }

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
                if(player.isPlaying()){
                }else {
                    MyApplication.type=1;
                    recreate();
                }
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
                Intent intent=new Intent(INSTANCE, LoginEmailActivity.class);
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
        //退出当前账户    `
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MiddleDialog(INSTANCE,null,INSTANCE.getString(R.string.tishi100),new MiddleDialog.onButtonCLickListener2() {
                    @Override
                    public void onActivieButtonClick(Object bean, int po) {
                        if(bean==null){
                        }else {
                            getData();
                        }
                    }
                }, R.style.registDialog).show();

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
                if(boo==true){
                    if(isChecked==true){
                        //开启指纹锁
                        boo=false;
                        startZhiwen(true);
                    } else {
                        //关闭指纹锁
                        boo=false;
                        startZhiwen(false);
                        //new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.main11),R.style.registDialog).show();
                    }
                }else {
                    boo=true;
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
            iv.setImageURI(null);
            MyApplication.type=1;
            recreate();
        }
    }


    //手动开启相机权限
    private void quan(){
       /* if(ContextCompat.checkSelfPermission(INSTANCE, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            Log.i("dcz2","没有权限");
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Log.i("dcz2","有权限");
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
          *//*  Intent intent=new Intent(INSTANCE, ScanActivity.class);
            startActivity(intent);*//*
        }*/
        if(NotificationsUtils.cameraIsCanUse()==true){
            Log.i("dcz2","有权限");
            Intent intent=new Intent(INSTANCE, ScanActivity.class);
            startActivity(intent);
        }else {
            Log.i("dcz2","没有权限");
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(NotificationsUtils.cameraIsCanUse()==true){
                Log.i("dcz2","有权限");
                Intent intent=new Intent(INSTANCE, ScanActivity.class);
                startActivity(intent);
            }else {
                Log.i("dcz2","没有权限");
            }
           /* if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }else{
                //权限被拒绝
                Log.i("dcz","权限被拒绝");
            }*/
        }
    }
    /**
     *   指纹身份验证这里开始
     * */
    private void startZhiwen(Boolean bo){
        //初始化指纹.
        fingerprintManager = FingerprintManagerCompat.from(this);
        //先判断有没有指纹传感器
        if (!fingerprintManager.isHardwareDetected()) {
            // 没有检测到指纹传感器，显示对话框告诉用户
            button2.setChecked(false);
            boo=true;
            new MiddleDialog(INSTANCE,this.getString(R.string.no_sensor_dialog_title),R.style.registDialog).show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 没有一个指纹图像被登记
            button2.setChecked(false);
            boo=true;
            new MiddleDialog(INSTANCE,this.getString(R.string.no_fingerprint_enrolled_dialog_title),R.style.registDialog).show();
        } else {
            //弹框让用户确认指纹
            setDialog(this.getString(R.string.tishi92),bo);
        }
    }
    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
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
                            Intent intent=new Intent(INSTANCE, LoginEmailActivity.class);
                            startActivity(intent);
                            ActivityUtils.getInstance().popActivity(INSTANCE);
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
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                    Log.d("dcz_数据获取失败",response.message());
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof MainActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }

    /***
     *  登录
     * */
    public void login(String a,String b){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().login(a,b,null,MyApplication.pri_key,MyApplication.device,MyApplication.xinghao,MyApplication.rid).enqueue(new Callback<LoginOkBean>() {
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
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof MainActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        isForeground = true;
        super.onPause();
    }

    private void Mp3(){
        tv.setVisibility(View.GONE);
        /*thread = null;
        thread = new timeThread();
        thread.start();*/
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                player.start();
            }
        };
        timer.schedule(task,200);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
        iv.setImageURI(null);
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
                    if(type.equals("2")){//下线通知
                        ActivityUtils.getInstance().popAllActivities();
                        Intent inten=new Intent(INSTANCE, LoginEmailActivity.class);
                        startActivity(inten);
                    }
                }
            } catch (Exception e){
            }
        }
    }

    private void setDialog(String content, final Boolean bo){
        if(dia!=null){
            if(dia.isShowing()){
                dia.dismiss();
            }
        }
        start();
        dia=new MiddleDialog(INSTANCE,content,0,new MiddleDialog.onButtonCLickListener(){
            @Override
            public void onButtonCancel(String string) {
                //是空的时候用户点的取消，否则就是指纹验证成功的回调
                if(string==null){
                    Log.i("dcz","用户点击了取消");
                    if(cancellationSignal!=null){
                        Log.i("dcz","用户取消");
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                    }
                    dia.dismiss();
                    if(MyApplication.zhiwen==true){
                        button2.setChecked(true);
                    }else {
                        button2.setChecked(false);
                    }
                }else {
                    boo=true;
                    if(bo==false){
                        MyApplication.zhiwen=false;MyApplication.sf.edit().putBoolean("zhiwen", false).commit();
                    }else {
                        MyApplication.zhiwen=true;MyApplication.sf.edit().putBoolean("zhiwen", true).commit();
                    }
                    dia.dismiss();
                }
            }
        },R.style.registDialog);
        dia.show();
    }
    private void start(){
        // 指纹身份验证这里开始。
        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (cancellationSignal == null) {
                cancellationSignal = new CancellationSignal();
            }
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    cancellationSignal, myAuthCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(INSTANCE,R.string.setting_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
     /*   JPushInterface.stopPush(INSTANCE.getApplicationContext());
        ActivityUtils.getInstance().popActivity(this);*/
        if(KeyEvent.KEYCODE_BACK==keyCode){
            Intent home=new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return false ;
        }
        return super.onKeyDown(keyCode, event);
    }
    /***
     *  验证版本
     * */
    public void getVersion(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        HttpServiceClient.getInstance().version(MyApplication.device,"android",version,"1").enqueue(new Callback<VersionBean>() {
            @Override
            public void onResponse(Call<VersionBean> call, Response<VersionBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        Log.i("dcz_1",response.body().getData().getLatestVersion()+"q");
                        Log.i("dcz_2",version+"q");
                        setdialog();//判断是否开启通知
                        Log.i("dcz_比较当前版本与服务器",response.body().getData().getLatestVersion().compareTo(version)+"a");
                        if(response.body().getData().getLatestVersion()!=null){
                            if(response.body().getData().getLatestVersion().compareTo(version)==1){
                                path=response.body().getData().getPath().toString();
                                //强制更新版本
                                if(response.body().getData().getNeededUpdated().equals("1")){
                                    down();
                                }else {
                                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.system_download_description),INSTANCE.getString(R.string.tishi118)+response.body().getData().getLatestVersion(),new MiddleDialog.onButtonCLickListener2() {
                                        @Override
                                        public void onActivieButtonClick(Object bean, int po) {
                                            if(bean==null){
                                            }else {
                                                down();
                                            }
                                        }
                                    }, R.style.registDialog).show();
                                }
                            }
                        }

                    }else {
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<VersionBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof MainActivity){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    private void down(){
        UpdaterConfig config = new UpdaterConfig.Builder(INSTANCE)
                .setTitle(getResources().getString(R.string.app_name))
                .setDescription(getString(R.string.system_download_description))
                .setFileUrl(path)
                .setCanMediaScanner(true)
                .build();
        Updater.get().showLog(true).download(config);
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
                    Thread.sleep(3500);
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
                    }else {
                        dra= Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true).setUri(Uri.parse("asset://com.example.duan.chao/agif.gif")).build();
                        iv.setController(dra);
                    }
                }
            }
        };
    }
    private void setAnimation(int id,ImageView iv){
        Animation operatingAnim = AnimationUtils.loadAnimation(INSTANCE, id);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            iv.startAnimation(operatingAnim);
        }
    }
}
