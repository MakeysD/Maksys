package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *     登录
 *
 * */
public class LoginActivity extends BaseActivity {
    private LoginActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.xian1)
    TextView xian1;
    @BindView(R.id.xian2)
    TextView xian2;
    @BindView(R.id.xian3)
    TextView xian3;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;

    @BindView(R.id.button)
    TextView button;        //下一步
    @BindView(R.id.button2)
    TextView button2;       //忘记密码
    @BindView(R.id.et_guo)
    EditText guo;           //国家
    @BindView(R.id.quhao)
    EditText quhao;         //区号
    @BindView(R.id.et_phone)
    EditText phone;         //手机
    @BindView(R.id.et_mima)
    EditText mima;          //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {

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
                Intent intent=new Intent(INSTANCE,SmsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LookPasswordActivity.class);
                startActivity(intent);
            }
        });

        //国家
        guo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    iv1.setImageResource(R.mipmap.login1);
                    xian1.setBackgroundColor(Color.parseColor("#0581c6"));
                    iv2.setImageResource(R.mipmap.login02);
                    xian2.setBackgroundColor(Color.parseColor("#343436"));
                    iv3.setImageResource(R.mipmap.login03);
                    xian3.setBackgroundColor(Color.parseColor("#343436"));
                }
            }
        });
        guo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(phone.getText().toString().length()>0&&mima.getText().toString().length()>0){
                        button.setVisibility(View.VISIBLE);
                    }else {
                        button.setVisibility(View.GONE);
                    }
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //区号
        quhao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                iv1.setImageResource(R.mipmap.login01);
                xian1.setBackgroundColor(Color.parseColor("#343436"));
                iv2.setImageResource(R.mipmap.login2);
                xian2.setBackgroundColor(Color.parseColor("#0581c6"));
                iv3.setImageResource(R.mipmap.login03);
                xian3.setBackgroundColor(Color.parseColor("#343436"));
            }
        });

        //手机
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                iv1.setImageResource(R.mipmap.login01);
                xian1.setBackgroundColor(Color.parseColor("#343436"));
                iv2.setImageResource(R.mipmap.login2);
                xian2.setBackgroundColor(Color.parseColor("#0581c6"));
                iv3.setImageResource(R.mipmap.login03);
                xian3.setBackgroundColor(Color.parseColor("#343436"));
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(guo.getText().toString().length()>0&&mima.getText().toString().length()>0){
                        button.setVisibility(View.VISIBLE);
                    }else {
                        button.setVisibility(View.GONE);
                    }
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //密码
        mima.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                iv1.setImageResource(R.mipmap.login01);
                xian1.setBackgroundColor(Color.parseColor("#343436"));
                iv2.setImageResource(R.mipmap.login02);
                xian2.setBackgroundColor(Color.parseColor("#343436"));
                iv3.setImageResource(R.mipmap.login3);
                xian3.setBackgroundColor(Color.parseColor("#0581c6"));
            }
        });

        mima.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(guo.getText().toString().length()>0&&phone.getText().toString().length()>0){
                        button.setVisibility(View.VISIBLE);
                    }else {
                        button.setVisibility(View.GONE);
                    }
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
