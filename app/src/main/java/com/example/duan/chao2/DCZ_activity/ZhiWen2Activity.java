package com.example.duan.chao2.DCZ_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao2.DCZ_application.MyApplication;
import com.example.duan.chao2.DCZ_bean.HaveBean;
import com.example.duan.chao2.DCZ_jiguang.ExampleUtil;
import com.example.duan.chao2.DCZ_jiguang.LocalBroadcastManager;
import com.example.duan.chao2.DCZ_lockdemo.LockUtil;
import com.example.duan.chao2.DCZ_selft.MiddleDialog;
import com.example.duan.chao2.DCZ_util.ActivityUtils;
import com.example.duan.chao2.DCZ_zhiwen.CryptoObjectHelper;
import com.example.duan.chao2.DCZ_zhiwen.MyAuthCallback;
import com.example.duan.chao2.R;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhiWen2Activity extends BaseActivity {
    private Boolean jiesuo=false;
    private Boolean jiaoyi=false;
    private String messge;
    private ZhiWen2Activity INSTANCE;
    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    private int[] mIndexs;
    private Handler handler = null;
    private int number=0;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CONTENT_TYPE = "content_type";
    public static final String KEY_EXTRAS = "extras";
    private String type;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.result2)
    TextView result2;
    @BindView(R.id.change)
    TextView change;        //去进行手势解锁
    @BindView(R.id.wangji)
    TextView wangji;        //忘记密码
    @BindView(R.id.cancle)
    TextView cancle;        //取消
    @BindView(R.id.zhiwen)
    RelativeLayout zhiwen;
    @BindView(R.id.zhiwen_start)
    LinearLayout start;
    @BindView(R.id.ll_canale)   //验证错误一次后的弹框布局
            LinearLayout ll_cancle;
    @BindView(R.id.cancle2)
    TextView cancle2;
    @BindView(R.id.shoushi)
    TextView shoushi;
    @BindView(R.id.name)
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_wen2);
        INSTANCE=this;
        ButterKnife.bind(this);
        registerMessageReceiver();
        result2.setVisibility(View.VISIBLE);
        MyApplication.zhiwen_namber=0;
        number=0;
        setViews();
        setListener();
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

    private void setViews() {
        name.setText(MyApplication.username);
        mIndexs= LockUtil.getPwd(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        result.setText(R.string.fingerprint_success);
                        cancellationSignal = null;
                        MyApplication.zhiwen_namber=0;
                        number=0;
                        Intent i=new Intent();
                        setResult(2,i);
                        ActivityUtils.getInstance().popActivity(INSTANCE);
                        break;
                    case MSG_AUTH_FAILED:
                        number=number+1;
                        result.setText(R.string.fingerprint_not_recognized);
                        result.startAnimation(AnimationUtils.loadAnimation(INSTANCE, R.anim.shake));
                        cancellationSignal = null;
                        ll_cancle.setVisibility(View.VISIBLE);
                        if(number<5){
                            start();
                        }else {
                            zhiwen.setVisibility(View.GONE);
                            MyApplication.zhiwen_namber=MyApplication.zhiwen_namber+1;
                        }
                        break;
                    case MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };

        //初始化指纹.
        fingerprintManager = FingerprintManagerCompat.from(this);
        //先判断有没有指纹传感器
        if (!fingerprintManager.isHardwareDetected()) {
            new MiddleDialog(INSTANCE,this.getString(R.string.no_sensor_dialog_title),R.style.registDialog).show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            new MiddleDialog(INSTANCE,this.getString(R.string.no_fingerprint_enrolled_dialog_title),R.style.registDialog).show();
        } else {
            try {
                myAuthCallback = new MyAuthCallback(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start();
    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancellationSignal !=null){
                    cancellationSignal.cancel();
                    cancellationSignal = null;
                }
                zhiwen.setVisibility(View.GONE);
            }
        });
        cancle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellationSignal.cancel();
                cancellationSignal = null;
                zhiwen.setVisibility(View.GONE);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.zhiwen_namber>1){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.ErrorLockout_warning),R.style.registDialog).show();
                }else {
                    ll_cancle.setVisibility(View.GONE);
                    zhiwen.setVisibility(View.VISIBLE);
                    start();
                }
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
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否开启密码
                if(LockUtil.getPwdStatus(INSTANCE)){
                    Intent intent=new Intent(INSTANCE,StartLock2Activity.class);
                    startActivity(intent);
                    ActivityUtils.getInstance().popActivity(INSTANCE);
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.setti),R.style.registDialog).show();
                }
             /*   //判断当前是否设置过密码
                if(mIndexs.length>1){
                    Intent intent=new Intent(INSTANCE,StartLockActivity.class);
                    intent.putExtra("type","1");
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(INSTANCE,R.string.setti, Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        shoushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否开启密码
                if(LockUtil.getPwdStatus(INSTANCE)){
                    Intent intent=new Intent(INSTANCE,StartLock2Activity.class);
                    startActivity(intent);
                    ActivityUtils.getInstance().popActivity(INSTANCE);
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.setti),R.style.registDialog).show();
                }
            }
        });
    }
    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                result.setText(R.string.AcquiredGood_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                result.setText(R.string.AcquiredImageDirty_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                result.setText(R.string.AcquiredInsufficient_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                result.setText(R.string.AcquiredPartial_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                result.setText(R.string.AcquiredTooFast_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                result.setText(R.string.AcquiredToSlow_warning);
                break;
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            //取消了传感器的使用
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                // result.setText(R.string.ErrorCanceled_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                result.setText(R.string.ErrorHwUnavailable_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                //指纹锁定了
                MyApplication.zhiwen_namber=MyApplication.zhiwen_namber+1;
                result.setText(R.string.ErrorLockout_warning);
                result2.setVisibility(View.GONE);
                MyApplication.zhiwen_namber=2;
                number=5;
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                result.setText(R.string.ErrorNoSpace_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                result.setText(R.string.ErrorTimeout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                result.setText(R.string.ErrorUnableToProcess_warning);
                break;
        }
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
    protected void onDestroy() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
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
}
