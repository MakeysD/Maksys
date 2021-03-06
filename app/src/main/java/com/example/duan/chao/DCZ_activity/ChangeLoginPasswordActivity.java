package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.AsterPassword;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
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
    @BindView(R.id.x1)
    ImageView x1;
    @BindView(R.id.x2)
    ImageView x2;
    @BindView(R.id.x3)
    ImageView x3;
    @BindView(R.id.checkBox1)
    CheckBox checkBox1;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    @BindView(R.id.checkBox3)
    CheckBox checkBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_login_password);
        INSTANCE=this;
        ButterKnife.bind(this);
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
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
        if(ContentUtil.isMobileNO(b)){
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
            tv5.setText(this.getString(R.string.tishia68)+b.substring(0,3)+c+d+"@"+e+this.getString(R.string.tishi68));
        }else {
            tv5.setText(this.getString(R.string.tishia68)+MyApplication.username+this.getString(R.string.tishi68));
        }

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
        x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et2.setText("");
            }
        });
        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et3.setText("");
            }
        });
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
                if(et2.getText().length()>0){
                    x2.setVisibility(View.VISIBLE);
                }else {
                    x2.setVisibility(View.GONE);
                }
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
                if(et3.getText().length()>0){
                    x3.setVisibility(View.VISIBLE);
                }else {
                    x3.setVisibility(View.GONE);
                }
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
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    et1.setTransformationMethod(new AsterPassword());
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    et2.setTransformationMethod(new AsterPassword());
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    et3.setTransformationMethod(new AsterPassword());
                }
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
        dialog.show();
        String random= DSA.md5(RandomUtil.RandomNumber());
        String a = DSA.md5(et3.getText().toString());
        String b = DSA.md5(et2.getText().toString());
        String c = DSA.md5(et1.getText().toString());
        String z="0";
        String str ="code="+z+"&confirmNewPwd="+a+"&newPwd="+b+"&nonce="+random+"&oldPwd="+c;
        byte[] data = str.getBytes();
        try {
            String sign = DSA.sign(data, MyApplication.pri_key);
            HttpServiceClient.getInstance().login_password(z,c,b,a,random,sign).enqueue(new Callback<LoginOkBean>() {
                @Override
                public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                    dialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body()!=null){
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
                                MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                                new MiddleDialog(INSTANCE,null,INSTANCE.getString(R.string.tishi85),true,new MiddleDialog.onButtonCLickListener2() {
                                    @Override
                                    public void onActivieButtonClick(Object bean, int po) {
                                        if(bean==null){
                                        }else {
                                            ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                        }
                                        ActivityUtils.getInstance().popActivity(INSTANCE);
                                    }
                                }, R.style.registDialog).show();
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
                    if(ActivityUtils.getInstance().getCurrentActivity() instanceof ChangeLoginPasswordActivity){
                        dialog.dismiss();
                        new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
