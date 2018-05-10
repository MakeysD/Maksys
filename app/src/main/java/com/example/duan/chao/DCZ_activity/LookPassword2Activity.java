package com.example.duan.chao.DCZ_activity;
import android.app.Dialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      找回密码(用邮箱号找回密码)
 *
 * */
public class LookPassword2Activity extends BaseActivity {
    private LookPassword2Activity INSTANCE;
    private Handler handler;
    private List<CityBean> list;
    private timeThread thread;
    private Dialog dialog;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.xian2)
    TextView xian2;
    @BindView(R.id.xian3)
    TextView xian3;
    @BindView(R.id.xian4)
    TextView xian4;
    @BindView(R.id.xian5)
    TextView xian5;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.iv5)
    ImageView iv5;

    @BindView(R.id.button)
    TextView button;        //下一步
    @BindView(R.id.et_phone)
    EditText phone;         //手机
    @BindView(R.id.et_code)
    EditText ed_code;       //验证码
    @BindView(R.id.code)
    TextView tv_code;       //验证码
    @BindView(R.id.et_mima4)
    EditText mima;          //密码
    @BindView(R.id.et_mima5)
    EditText mima2;         //确定密码
    @BindView(R.id.checkBox)
    CheckBox yan;           //眼睛
    @BindView(R.id.checkBox2)
    CheckBox yan2;           //眼睛
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_password2);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        mima.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mima2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        ShebeiUtil.setEdNoChinaese(mima);
        ShebeiUtil.setEdNoChinaese(mima2);
        ShebeiUtil.setEdit(mima);
        ShebeiUtil.setEdit(mima2);
        setViews();
        setListener();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
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
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mima.getText().toString().equals(mima2.getText().toString())){
                    getData();
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.lock9),R.style.registDialog).show();
                }
            }
        });
        yan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mima.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //替换成*号
                    // mima.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mima.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
            }
        });
        yan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mima2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //替换成*号
                    // mima.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mima2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
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
                    if(ed_code.getText().toString().length()>5&&
                            mima.getText().toString().length()>0&&
                            mima2.getText().toString().length()>0){
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

        //验证码
        ed_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                type3();
            }
        });
        ed_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(ed_code.getText().toString().length()>5&&
                            phone.getText().toString().length()>0&&
                            mima2.getText().toString().length()>0){
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
                type4();
            }
        });
        mima.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(ed_code.getText().toString().length()>5&&
                            phone.getText().toString().length()>0&&
                            mima2.getText().toString().length()>0){
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
        //确认密码
        mima2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                type5();
            }
        });
        mima2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    if(ed_code.getText().toString().length()>5&&
                            mima.getText().toString().length()>0&&
                            phone.getText().toString().length()>0){
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
                //  if (ContentUtil.isMobileNO(phone.getText().toString())) {  //如果输入的手机格式正确
                if(phone.getText().length()>0){
                    getSms();
                    tv_code.setBackgroundResource(R.drawable.yuanjiaohui);       //设置成灰色
                    tv_code.setTextColor(getResources().getColor(R.color.white));
                    tv_code.setEnabled(false);                     //设置不可点击
                    thread = null;
                    thread = new timeThread();
                    thread.start();
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi74),R.style.registDialog).show();
                }
              /*  }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi74),R.style.registDialog).show();
                }*/

            }
        });

    }


    private void type1(){
        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login04);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        iv4.setImageResource(R.mipmap.login03);
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        iv5.setImageResource(R.mipmap.login03);
        xian5.setBackgroundColor(Color.parseColor("#343436"));

        Intent intent=new Intent(INSTANCE, CityListActivity.class);
        startActivity(intent);
    }

    private void type2(){
        iv2.setImageResource(R.mipmap.login2);
        xian2.setBackgroundColor(Color.parseColor("#0581c6"));
        iv3.setImageResource(R.mipmap.login04);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        iv4.setImageResource(R.mipmap.login03);
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        iv5.setImageResource(R.mipmap.login03);
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }

    private void type3(){
        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login4);
        xian3.setBackgroundColor(Color.parseColor("#0581c6"));
        iv4.setImageResource(R.mipmap.login03);
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        iv5.setImageResource(R.mipmap.login03);
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type4(){
        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login04);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        iv4.setImageResource(R.mipmap.login3);
        xian4.setBackgroundColor(Color.parseColor("#0581c6"));
        iv5.setImageResource(R.mipmap.login03);
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type5(){
        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login04);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        iv4.setImageResource(R.mipmap.login03);
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        iv5.setImageResource(R.mipmap.login3);
        xian5.setBackgroundColor(Color.parseColor("#0581c6"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '*';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

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
    /***
     * 验证码
     * */
    public void getSms(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().sendsms(phone.getText().toString()+"@qeveworld.com","5",null).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("10516")){
                        MyApplication.sf.edit().putString("cookie","").commit();
                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                        new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int position) {
                                ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                ActivityUtils.getInstance().popAllActivities();
                            }
                        }, R.style.registDialog).show();
                        return;
                    }
                    if(response.body().getCode().equals("20000")){

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
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LookPassword2Activity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }

    /***
     *  找回密码
     * */
    public void getData(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().fogotpwd(phone.getText().toString()+"@qeveworld.com",ed_code.getText().toString(),null, DSA.md5(mima.getText().toString()),DSA.md5(mima2.getText().toString())).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("10516")){
                        MyApplication.sf.edit().putString("cookie","").commit();
                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                        new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int position) {
                                ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                ActivityUtils.getInstance().popAllActivities();
                            }
                        }, R.style.registDialog).show();
                        return;
                    }
                    if(response.body().getCode().equals("20000")){
                        new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi111),new MiddleDialog.onButtonCLickListener(){
                            @Override
                            public void onButtonCancel(String string) {
                                ActivityUtils.getInstance().popActivity(INSTANCE);
                            }
                        },R.style.registDialog).show();
                    }else {
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LookPassword2Activity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
