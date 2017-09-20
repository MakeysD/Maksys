package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      修改支付密码
 *
 * */
public class ChangePayPasswordActivity extends BaseActivity {
    private ChangePayPasswordActivity INSTANCE;
    private Dialog dialog;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.textView5)
    TextView tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_password);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        et1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        et2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        et3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        ShebeiUtil.setEdNoChinaese(et1);
        ShebeiUtil.setEdNoChinaese(et2);
        ShebeiUtil.setEdNoChinaese(et3);
        ShebeiUtil.setEdit(et1);
        ShebeiUtil.setEdit(et2);
        ShebeiUtil.setEdit(et3);
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        String[]  strs=MyApplication.username.split("@");
        String b = strs[0];
        String e = strs[1];
        b.substring(0,3);
        b.substring(b.length()-3,b.length());
        Log.i("dcz", b.substring(0,3));
        Log.i("dcz", b.substring(3,b.length()-3));
        Log.i("dcz",b.substring(b.length()-3,b.length()));
        String d=b.substring(b.length()-3,b.length());
        String a=b.substring(3,b.length()-3);
        String c=null;
        for(int i=0;i<a.length();i++){
            if(c==null){
                c="*";
            }else {
                c=c+"*";
            }
        }
        tv5.setText(this.getString(R.string.tishia68a)+b.substring(0,3)+c+d+"@"+e+this.getString(R.string.tishi68a));
    }
    /**
     *  监听
     * */
    private void setListener() {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>5&&et2.getText().toString().length()<19&&et3.getText().toString().length()>5&&et3.getText().toString().length()<19){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>5&&et2.getText().toString().length()<19&&et3.getText().toString().length()>5&&et3.getText().toString().length()<19){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>5&&et2.getText().toString().length()<19&&et3.getText().toString().length()>5&&et3.getText().toString().length()<19){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et2.getText().toString().equals(et3.getText().toString())){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.code14),R.style.registDialog).show();
                }else if(et1.getText().toString().equals(et2.getText().toString())){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi109),R.style.registDialog).show();
                }else {
                    getData();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
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
        HttpServiceClient.getInstance().anquan_password(DSA.md5(et1.getText().toString()),DSA.md5(et2.getText().toString()),DSA.md5(et3.getText().toString()),"0").enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),new MiddleDialog.onButtonCLickListener(){
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
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof ChangePayPasswordActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
