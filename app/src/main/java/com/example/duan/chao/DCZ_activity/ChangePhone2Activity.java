package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.R;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.duan.chao.DCZ_activity.CityListActivity.jsonToList;

/**
 *  更换密保手机（新手机号验证）
 *
 * */
public class ChangePhone2Activity extends BaseActivity {
    private ChangePhone2Activity INSTANCE;
    private List<CityBean> list;
    public static String code="";
    private Handler handler;
    private timeThread thread;
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
    TextView button;        //确定
    @BindView(R.id.et_guo)
    LinearLayout ll_guo;    //国家
    @BindView(R.id.tv_guo)
    TextView guo;           //国家
    @BindView(R.id.quhao)
    EditText quhao;         //区号
    @BindView(R.id.et_phone)
    EditText phone;         //手机
    @BindView(R.id.et_mima)
    EditText mima;
    @BindView(R.id.jia)
    TextView jia;
    @BindView(R.id.code)
    TextView tv_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone2);
        ButterKnife.bind(this);
        INSTANCE=this;
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        setViews();
        setListener();
    }

    /**
     *  数据初始化
     * */
    private void setViews() {
        code="";
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
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getData();
                if (ContentUtil.isMobileNO(phone.getText().toString())) {  //如果输入的手机格式正确
                    Intent intent=new Intent(INSTANCE,SmsActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(INSTANCE,R.string.tishi74, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //国家
        guo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    type1();
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
        ll_guo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type1();
            }
        });
        //区号
        quhao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                type2();
            }
        });
        quhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type2();
            }
        });
        quhao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String content = ToString(INSTANCE.getAssets().open("city.json"), "UTF-8");
                    list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
                    Log.i("dcz",list.toString());
                    for(int i=0;i<list.size();i++){
                        if(s.toString().equals(String.valueOf(list.get(i).getCountry_code()))){
                            guo.setText(list.get(i).getCountry_name_cn());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //手机
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                type2();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type2();
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
        mima.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                type3();
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
        tv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_code.setBackgroundResource(R.drawable.yuanjiaohui);       //设置成灰色
                tv_code.setTextColor(getResources().getColor(R.color.white));
                tv_code.setEnabled(false);                     //设置不可点击
                thread = null;
                thread = new timeThread();
                thread.start();
            }
        });
    }

    private void type1(){
        iv1.setImageResource(R.mipmap.login1);
        xian1.setBackgroundColor(Color.parseColor("#0581c6"));
        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login03);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        jia.setTextColor(Color.parseColor("#a2a2a2"));
        Intent intent=new Intent(INSTANCE, CityListActivity.class);
        startActivity(intent);
    }

    private void type2(){
        iv1.setImageResource(R.mipmap.login01);
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        iv2.setImageResource(R.mipmap.login2);
        xian2.setBackgroundColor(Color.parseColor("#0581c6"));
        iv3.setImageResource(R.mipmap.login03);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        jia.setTextColor(Color.parseColor("#ffffff"));
    }

    private void type3(){
        iv1.setImageResource(R.mipmap.login01);
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login3);
        xian3.setBackgroundColor(Color.parseColor("#0581c6"));
        jia.setTextColor(Color.parseColor("#a2a2a2"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        quhao.setText(code);
        jia.setTextColor(Color.parseColor("#ffffff"));
    }

    public static String ToString(InputStream is, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line).append("\n");
                }
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
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
                        tv_code.setBackgroundResource(R.drawable.yuanjiaohui);
                        tv_code.setTextColor(getResources().getColor(R.color.text02));
                        tv_code.setText(num +INSTANCE.getString(R.string.tishi75));
                        tv_code.setEnabled(false);                     //设置不可点击
                    } else {             //如果剩余秒数为0，设置按钮可点击
                        tv_code.setBackgroundResource(R.drawable.yuanjiaolan);
                        tv_code.setTextColor(0xffffffff);
                        tv_code.setText(R.string.tishi76);
                        tv_code.setEnabled(true);                     //设置可点击
                    }
                }
            }
        };
    }
}
