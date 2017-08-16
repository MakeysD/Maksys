package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      修改登录密码
 *
 * */
public class ChangeLoginPasswordActivity extends BaseActivity {
    private ChangeLoginPasswordActivity INSTANCE;
    private Dialog dialog;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.textView5)
    TextView tv5;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_login_password);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        setViews();
        setListener();
    }
    /**
     *  初始化
     * */
    private void setViews() {
        tv5.setText(this.getString(R.string.tishi67)+"132****8723"+this.getString(R.string.tishi68));
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
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>0&&et3.getText().toString().length()>0){
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
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>0&&et3.getText().toString().length()>0){
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
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>0&&et3.getText().toString().length()>0){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().login_password("0",et1.getText().toString(),et2.getText().toString(),et3.getText().toString()).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Intent intent=new Intent(INSTANCE,LoginActivity.class);
                            startActivity(intent);
                            ActivityUtils.getInstance().popAllActivities();
                        }else {
                           // if(MyApplication.language.equals("ENGLISH")){
                            new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                           /* }else {
                                new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                            }*/
                            finish();
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
                dialog.dismiss();
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
