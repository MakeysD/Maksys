package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      短信验证
 *
 * */
public class SmsActivity extends BaseActivity {
    private SmsActivity INSTANCE;
    private Handler handler;
    private timeThread thread;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;       //下一步
    @BindView(R.id.code)
    TextView code;       //验证码
    @BindView(R.id.et_code)
    EditText et_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        newhandler();                                       //新建handler处理消息
    }
    /**
     *  监听
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,MainActivity.class);
                startActivity(intent);
                MyApplication.isLogin=true;
                finish();
            }
        });
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code.setBackgroundResource(R.drawable.yuanjiaohui);       //设置成灰色
                code.setTextColor(getResources().getColor(R.color.white));
                code.setEnabled(false);                     //设置不可点击
                thread = null;
                thread = new timeThread();
                thread.start();
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=4){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
}
