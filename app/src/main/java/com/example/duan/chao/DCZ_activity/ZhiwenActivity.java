package com.example.duan.chao.DCZ_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_zhiwen.CryptoObjectHelper;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhiwenActivity extends BaseActivity {
    private ZhiwenActivity INSTANCE;
    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;

    private Handler handler = null;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.change)
    TextView change;        //去进行手势解锁
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhiwen);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
    }

    private void setViews() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        result.setText("指纹识别成功");
                        cancellationSignal = null;
                        Intent intent=new Intent(INSTANCE, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case MSG_AUTH_FAILED:
                        result.setText("指纹识别失败，请再试一次！");
                        cancellationSignal = null;
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
            // 没有检测到指纹传感器，显示对话框告诉用户
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
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            Toast.makeText(INSTANCE, "指纹初始化失败!再试一次!", Toast.LENGTH_SHORT).show();
        }
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
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                result.setText(R.string.ErrorCanceled_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                result.setText(R.string.ErrorHwUnavailable_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                result.setText(R.string.ErrorLockout_warning);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }
}
