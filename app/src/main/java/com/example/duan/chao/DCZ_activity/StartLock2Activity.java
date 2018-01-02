package com.example.duan.chao.DCZ_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_jiguang.ExampleUtil;
import com.example.duan.chao.DCZ_jiguang.LocalBroadcastManager;
import com.example.duan.chao.DCZ_lockdemo.CustomLockView;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_lockdemo.ScreenObserver;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartLock2Activity extends BaseActivity {
    private StartLock2Activity INSTANCE;
    private Boolean jiesuo=false;
    private Boolean jiaoyi=false;
    private String messge;
    private String type;
    public <K extends View> K getViewById(int id) {
        return (K) getWindow().findViewById(id);
    }
    private ScreenObserver mScreenObserver;
    private TextView tvWarn;
    private int[] mIndexs;

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CONTENT_TYPE = "content_type";
    public static final String KEY_EXTRAS = "extras";

    @BindView(R.id.change)
    TextView change;        //去进行指纹解锁
    @BindView(R.id.wangji)
    TextView wangji;        //忘记密码
    @BindView(R.id.name)
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_lock2);
        INSTANCE=this;
        ButterKnife.bind(this);
        registerMessageReceiver();
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenStateChange(boolean isScreenOn) {
                if (!isScreenOn&& LockUtil.getPwdStatus(INSTANCE)&& LockUtil.getPwd(INSTANCE).length>0) {
                    doSomethingOnScreenOff();
                }
            }
        });
        initView();
        setListener();
        mIndexs= LockUtil.getPwd(this);
        //判断当前是否设置过密码，没有设置过，直接跳转到设置手势密码页面
        if(mIndexs.length>1){
            final CustomLockView cl=(CustomLockView)findViewById(R.id.cl);
            cl.setmIndexs(mIndexs);
            cl.setErrorTimes(5);
            cl.setStatus(1);
            cl.setShow(false);
            cl.hui(new CustomLockView.OnType(){
                @Override
                public void ontate() {
                    tvWarn.setText(R.string.lock12);
                    tvWarn.setTextColor(getResources().getColor(R.color.red));
                    tvWarn.startAnimation(AnimationUtils.loadAnimation(INSTANCE, R.anim.shake));
                }
            });
            cl.setOnCompleteListener(new CustomLockView.OnCompleteListener() {
                @Override
                public void onComplete(int[] indexs) {
                    //修改密码或设置密码进来
                    Toast.makeText(INSTANCE,R.string.tishi73,Toast.LENGTH_SHORT).show();
                    Intent i=new Intent();
                    setResult(2,i);
                    ActivityUtils.getInstance().popActivity(INSTANCE);
                }
                @Override
                public void onError() {
                    if (cl.getErrorTimes() > 0) {
                        tvWarn.setText(INSTANCE.getString(R.string.lock7) + String.valueOf(cl.getErrorTimes()) +INSTANCE.getString(R.string.lock8));
                        tvWarn.setTextColor(getResources().getColor(R.color.red));
                    }else {
                        LockUtil.setPwdStatus(INSTANCE,false);
                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        Intent intent=new Intent(INSTANCE,LoginEmailActivity.class);
                        startActivity(intent);
                        ActivityUtils.getInstance().popActivity(INSTANCE);
                        Log.i("dcz","解锁已达到上限");
                    }
                }
            });
        }
    }


    private void setListener() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,ZhiwenActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        wangji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
                MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                MyApplication.zhiwen=false;MyApplication.sf.edit().putBoolean("zhiwen", false).commit();
                LockUtil.setPwdStatus(INSTANCE,false);
                Intent intent=new Intent(INSTANCE,LoginEmailActivity.class);
                startActivity(intent);
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
    }

    /**
     * 打开验证手势(黑屏再启动时才会调用)
     */
    private void doSomethingOnScreenOff() {
       /* Intent intent = new Intent();
        intent.setClass(getApplicationContext(), StartLockActivity.class);
        intent.putExtra("current","resume");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityUtils.getInstance().popActivity(INSTANCE);*/
    }

    /**
     * 初始化控件
     */
    private void initView(){
        tvWarn=getViewById(R.id.tvWarn);
        name.setText(MyApplication.username);
    }

    @Override
    protected void onDestroy() {
        if(mScreenObserver!=null){
            mScreenObserver.stopScreenStateUpdate();
        }
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
                Log.i("dcz",intent.getAction());
                messge = intent.getStringExtra(KEY_MESSAGE);
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

                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    Log.i("dcz","MESSAGE_RECEIVED_ACTION");
                    if(type.equals("2")){//下线通知
                        ActivityUtils.getInstance().popAllActivities();
                        Intent inten=new Intent(INSTANCE, LoginEmailActivity.class);
                        startActivity(inten);
                    }else if(type.equals("1")){
                        jiesuo=true;
                    }else {
                        jiaoyi=true;
                    }
                }
            } catch (Exception e){
            }
        }
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(KeyEvent.KEYCODE_BACK==keyCode){
            Intent home=new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return false ;
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
