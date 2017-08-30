package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  更换密保手机（验证原手机号）
 *
 * */
public class ChangePhone1Activity extends BaseActivity {
    private ChangePhone1Activity INSTANCE;
    private Handler handler;
    private timeThread thread;
    private Dialog dialog;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.button)
    TextView button;           //下一步
    @BindView(R.id.et_code)
    EditText et_code;          //验证码
    @BindView(R.id.code)
    TextView code;          //验证码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone1);
        ButterKnife.bind(this);
        INSTANCE=this;
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        ShebeiUtil.setEdit(et_code);
        setViews();
        setListener();
    }


    /**
     *  数据初始化
     * */
    private void setViews() {
        String phoneNumber = MyApplication.username.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        tv.setText(this.getString(R.string.tishi69)+phoneNumber);
        newhandler();
    }
    /**
     * 监听
     *
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( s.length()<4){
                    button.setVisibility(View.GONE);
                }else {
                    button.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ChangePhone2Activity.class);
                startActivity(intent);
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSms();
                code.setBackgroundResource(R.drawable.yuanjiaohui);       //设置成灰色
                code.setTextColor(getResources().getColor(R.color.white));
                code.setEnabled(false);                     //设置不可点击
                thread = null;
                thread = new timeThread();
                thread.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }
    private class timeThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 60; i >= 0; i--) {
                try {
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    msg.arg1 = i;         //秒数赋值给消息
                    handler.sendMessage(msg);
                    Thread.sleep(1000l);
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
                if (msg.what == 0) {
                    int num = msg.arg1;   //得到剩余秒数
                    if (num > 0) {
                        code.setBackgroundResource(R.drawable.yuanjiaohui);
                        code.setTextColor(getResources().getColor(R.color.text02));
                        code.setText(num + "秒后重发");
                        code.setEnabled(false);                     //设置不可点击
                    } else {             //如果剩余秒数为0，设置按钮可点击
                        code.setBackgroundResource(R.drawable.yuanjiaolan);
                        code.setTextColor(0xffffffff);
                        code.setText("重新获取");
                        code.setEnabled(true);                     //设置可点击
                    }
                }
            }
        };
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getSms(){
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        if(MyApplication.rid==null||MyApplication.rid.equals("")){
            MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
        }
        HttpServiceClient.getInstance().sendsms(MyApplication.username,"2",null).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){

                    }else {
                        new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
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
}
