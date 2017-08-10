package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.duan.chao.DCZ_activity.CityListActivity.jsonToList;

/**
 *     登录
 *
 * */
public class LoginActivity extends BaseActivity {
    private LoginActivity INSTANCE;
    private List<CityBean> list;
    public static String code="";
    private LoginOkBean data;
    private Dialog dialog;
    private String content;
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
    LinearLayout ll_guo;    //国家
    @BindView(R.id.tv_guo)
    TextView guo;           //国家
    @BindView(R.id.quhao)
    EditText quhao;         //区号
    @BindView(R.id.et_phone)
    EditText phone;         //手机
    @BindView(R.id.et_mima)
    EditText mima;          //密码
    @BindView(R.id.jia)
    TextView jia;           //+
    @BindView(R.id.checkBox)
    CheckBox yan;           //眼睛

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
        code="";
        try {
            content = ToString(INSTANCE.getAssets().open("city.json"), "UTF-8");
            list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
            Log.i("dcz",list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        guo.setFocusable(false);
        for(int i=0;i<list.size();i++){
            if(MyApplication.city.equals(String.valueOf(list.get(i).getCountry_name_cn()))){
                Log.i("dcz",list.get(i).getCountry_name_cn());
                Log.i("dcz",list.get(i).getCountry_code()+"");
                code=list.get(i).getCountry_code()+"";
            }
        }
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();

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
                getData();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LookPasswordActivity.class);
                startActivity(intent);
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

        //密码
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

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
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

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username","admin");
                map.put("password", "123456");
                map.put("code","");
                map.put("deviceUUID","866145033751781");
                map.put("deviceName","Redmi Note 4X");
                String response = OkHttpClientHelper.postKeyValuePair(INSTANCE,MyApplication.uri+"login", map, "getcode");
                Log.i("dcz_返回数据",response);
            }
        }).start();*/
       /* dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();*/
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
        HttpServiceClient.getInstance().login(phone.getText().toString(),mima.getText().toString(),MyApplication.device,MyApplication.xinghao,MyApplication.rid).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
                        data=response.body().getData();
                        MyApplication.first=false;MyApplication.sf.edit().putBoolean("first",false).commit();
                        if(MyApplication.token!=null&&!(MyApplication.token.equals(""))){
                            SharedPreferences sf2 = INSTANCE.getSharedPreferences("user2",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf2.edit();
                            editor.putString("token",MyApplication.token);
                            editor.putString("nickname",MyApplication.nickname);
                            editor.putString("username",MyApplication.username);
                            editor.putString("mima",mima.getText().toString());
                            editor.commit();
                        }
                        MyApplication.token=data.getRefreshToken();MyApplication.sf.edit().putString("token",data.getRefreshToken()).commit();
                        MyApplication.nickname=data.getNickname();MyApplication.sf.edit().putString("nickname",data.getNickname()).commit();
                        MyApplication.username=data.getUsername();MyApplication.sf.edit().putString("username",data.getUsername()).commit();
                        Intent intent=new Intent(INSTANCE,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                dialog.dismiss();
                Log.i("dcz异常",call.toString());
                Toast.makeText(INSTANCE, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
