package com.example.duan.chao.DCZ_selft;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_jiguang.ExampleUtil;
import com.example.duan.chao.DCZ_jiguang.LocalBroadcastManager;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_zhiwen.CryptoObjectHelper;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by enjoytouch-ad02 on 2015/8/5.
 */
public class MiddleDialog<E> extends Dialog {
    private onButtonCLickListener listener;
    private onButtonCLickListener2 listener2;
    private onOKListeners okListeners;
    private onUpdateListeners listeners3;
    private E bean;
    private int position;
    private View view;
    private String content;

    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    public static boolean isForeground = true;
    private Handler handler = null;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;
    private TextView cntent;
    private Context acontext;
    /**
     *     确认与取消
     *
     * */
    public MiddleDialog(Context context, String title,String content, final onButtonCLickListener2<E> listener, int theme) {
        super(context, theme);
        view = View.inflate(context, R.layout.dialog_middle2, null);
        setContentView(view);
        setCancelable(false);        //设置点击对话框以外的区域时，是否结束对话框
        ((TextView) view.findViewById(R.id.title)).setText(title);       //设置对话框的标题内容
        ((TextView) view.findViewById(R.id.content)).setText(content);
        this.listener2 = listener;
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
                listener2.onActivieButtonClick("1", position);
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
                listener2.onActivieButtonClick(null, position);
            }
        });
    /*    view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {      //确定
            @Override
            public void onClick(View v) {
                //if(bean!=null){
                dismiss();
                listener2.onActivieButtonClick(bean, position);
                // }
            }
        });*/
    }
    /**
     *      提示
     *
     * */
    public MiddleDialog(Context context,String content,int theme) {
        super(context, theme);
        view = View.inflate(context, R.layout.dialog, null);
        setContentView(view);
        setCancelable(false);        //设置点击对话框以外的区域时，是否结束对话框
        ((TextView) view.findViewById(R.id.content)).setText(content);
        view.findViewById(R.id.execute).setOnClickListener(new View.OnClickListener() {      //确定
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     *     指纹验证
     *
     * */
    public MiddleDialog(Context context, final String content, int a, final onButtonCLickListener listener,int theme) {
        super(context, theme);
        view = View.inflate(context, R.layout.dialog_lock, null);
        this.listener=listener;
        setContentView(view);
        setCancelable(false);        //设置点击对话框以外的区域时，是否结束对话框
        acontext=context;
        cntent=(TextView) view.findViewById(R.id.content);
        cntent.setText(content);
        view.findViewById(R.id.execute).setOnClickListener(new View.OnClickListener() {      //确定
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onButtonCancel(null);
            }
        });
        Handle();
        start();
    }

    /**
     *      下线通知
     *
     * */
    public MiddleDialog(Context context, String title,String content,String tishi, final onButtonCLickListener2<E> listener, int theme) {
        super(context, theme);
        view = View.inflate(context, R.layout.dialog_middle, null);
        setContentView(view);
        setCancelable(false);        //设置点击对话框以外的区域时，是否结束对话框
        ((TextView) view.findViewById(R.id.title)).setText(title);       //设置对话框的标题内容
        this.listener2 = listener;
        ((TextView) view.findViewById(R.id.content)).setText(content);
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
                listener2.onActivieButtonClick("2", position);
            }
        });
      /*  view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dismiss();
                listener2.onActivieButtonClick(null,0);
            }
        });*/
    }

    public interface onOKListeners{
        void onOkButton();
    }

    public interface onUpdateListeners{
        void onButton(String password);
    }

    public interface onButtonCLickListener{
       public void onButtonCancel(String string);
    }
    public interface onButtonCLickListener2<E>{
        public void onActivieButtonClick(E bean, int position);
    }
    public void resetData(E bean,int position) {
        this.bean=bean;
        this.position = position;
    }

    private void start(){
        //初始化指纹.
        fingerprintManager = FingerprintManagerCompat.from(acontext);
        try {
            myAuthCallback = new MyAuthCallback(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Toast.makeText(acontext,R.string.setting_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void Handle(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //识别成功
                    case MSG_AUTH_SUCCESS:
                        cntent.setText(acontext.getString(R.string.fingerprint_success));
                        cancellationSignal = null;
                        listener.onButtonCancel("1");
                        break;
                    //识别失败
                    case MSG_AUTH_FAILED:
                        cntent.setText(acontext.getString(R.string.fingerprint_not_recognized));
                        cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                        break;
                    case MSG_AUTH_ERROR:
                        Log.i("dcz","MSG_AUTH_ERROR");
                        handleErrorCode(msg.arg1);
                        break;
                    case MSG_AUTH_HELP:
                        Log.i("dcz","MSG_AUTH_HELP");
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };
    }
    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                cntent.setText(acontext.getString(R.string.AcquiredGood_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                cntent.setText(acontext.getString(R.string.AcquiredImageDirty_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                cntent.setText(acontext.getString(R.string.AcquiredGood_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                cntent.setText(acontext.getString(R.string.AcquiredPartial_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                cntent.setText(acontext.getString(R.string.AcquiredTooFast_warning));
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                cntent.setText(acontext.getString(R.string.AcquiredToSlow_warning));
                break;
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            //取消了传感器的使用
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                cntent.setText(acontext.getString(R.string.ErrorCanceled_warning));
                cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                cntent.setText(acontext.getString(R.string.ErrorHwUnavailable_warning));
                cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                //指纹锁定了
                MyApplication.zhiwen_namber=MyApplication.zhiwen_namber+1;
                //cntent.setText(acontext.getString(R.string.ErrorLockout_warning));
                cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                //停止推送服务
                JPushInterface.stopPush(acontext.getApplicationContext());
                MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                Intent intent=new Intent(acontext,LoginActivity.class);
                acontext.startActivity(intent);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                cntent.setText(acontext.getString(R.string.ErrorNoSpace_warning));
                cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                cntent.setText(acontext.getString(R.string.ErrorTimeout_warning));
                cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                cntent.setText(acontext.getString(R.string.ErrorUnableToProcess_warning));
                cntent.startAnimation(AnimationUtils.loadAnimation(acontext, R.anim.shake));
                break;
        }
    }
}
