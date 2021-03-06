package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.duan.chao.DCZ_authenticator.AccountDb;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      短信验证
 *
 * */
public class SmsActivity extends BaseActivity {
    private SmsActivity INSTANCE;
    private Handler handler;
    private timeThread thread;
    private Dialog dialog;
    private String phone;
    private String password;
    private LoginOkBean data;

    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;       //下一步
    @BindView(R.id.code)
    TextView code;       //验证码
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.change)
    TextView change;        //已换号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        INSTANCE=this;
        ButterKnife.bind(this);
        JPushInterface.resumePush(getApplicationContext());
        phone=getIntent().getStringExtra("phone");
        password=getIntent().getStringExtra("password");
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
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
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
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ChangePhone3Activity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(MyApplication.rid==null||MyApplication.rid.equals("")){
                    MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
                    if(MyApplication.rid==null||MyApplication.rid.equals("")){
                        ContentUtil.makeToast(INSTANCE,"RID为空");
                        return;
                    }
                }*/
                getData();
            }
        });
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSms();
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=6){
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
                        code.setText(num +INSTANCE.getString(R.string.tishi75));
                        code.setEnabled(false);                     //设置不可点击
                    } else {             //如果剩余秒数为0，设置按钮可点击
                        code.setBackgroundResource(R.drawable.yuanjiaolan);
                        code.setTextColor(0xffffffff);
                        code.setText(R.string.tishi76);
                        code.setEnabled(true);                     //设置可点击
                    }
                }
            }
        };
    }

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
        if(MyApplication.rid==null||MyApplication.rid.equals("")){
            MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
        }
        //MyApplication.rid="sidofugychfudisygrs";
        HttpServiceClient.getInstance().login(phone,password,et_code.getText().toString(),MyApplication.pub_key,MyApplication.device,MyApplication.xinghao,MyApplication.rid).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        data=response.body().getData();
                        MyApplication.first=false;MyApplication.sf.edit().putBoolean("first",false).commit();
                        LockUtil.setPwdStatus(INSTANCE,false);
                        MyApplication.zhiwen=false;MyApplication.sf.edit().putBoolean("zhiwen", false).commit();
                        if(MyApplication.token!=null&&!(MyApplication.token.equals(""))){
                            SharedPreferences sf2 = INSTANCE.getSharedPreferences("user2",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sf2.edit();
                            editor.putString("token",MyApplication.token);
                            editor.putString("nickname",MyApplication.nickname);
                            editor.putString("username",MyApplication.username);
                            editor.putString("mima",password);
                            editor.commit();
                        }
                        MyApplication.token=data.getRefreshToken();MyApplication.sf.edit().putString("token",data.getRefreshToken()).commit();
                        MyApplication.nickname=data.getNickname();MyApplication.sf.edit().putString("nickname",data.getNickname()).commit();
                        MyApplication.username=data.getUsername();MyApplication.sf.edit().putString("username",data.getUsername()).commit();
                        MyApplication.mobile=data.getMobile();MyApplication.sf.edit().putString("mobile",data.getMobile()).commit();
                        MyApplication.password=password;MyApplication.sf.edit().putString("password",password);
                        MyApplication.DEFAULT_INTERVAL=data.getDefaultIntervalInSecond();MyApplication.sf.edit().putInt("DEFAULT_INTERVAL",data.getDefaultIntervalInSecond()).commit();
                        MyApplication.PIN_LENGTH=  data.getTotpCodeLength();MyApplication.sf.edit().putInt("PIN_LENGTH",data.getTotpCodeLength()).commit();
                        AccountDb.OtpType mode = /*mType.getSelectedItemPosition() == AccountDb.OtpType.TOTP.value ?*/ AccountDb.OtpType.TOTP;// : AccountDb.OtpType.HOTP;
                        MyApplication.saveSecret(INSTANCE, data.getTotpSecretKey(),
                                getEnteredKey(data.getTotpSecretKey()),
                                null,
                                mode,
                                AccountDb.DEFAULT_HOTP_COUNTER);
                        MyApplication.offset= Long.valueOf(0);MyApplication.sf.edit().putLong("offset",0).commit();
                        if(MyApplication.Webkey!=null){
                            MyApplication.Webkey=null;
                            if (MyApplication.webWay.startsWith("makeys://AppStartActivity/identityauthorize")){
                                Intent intent = new Intent(INSTANCE, PersonDataActivity.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(INSTANCE, WebAuthorActivity.class);
                                startActivity(intent);
                            }
                        }else if(MyApplication.Ssokey!=null){
                            MyApplication.Ssokey=null;
                            Intent intent=new Intent(INSTANCE,AuthorActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent=new Intent(INSTANCE,MainActivity.class);
                            startActivity(intent);
                        }
                        ActivityUtils.getInstance().popAllActivities();
                    }else {
                        new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SmsActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
               /* if (t instanceof AddUpdate.MyThrow){

                }*/
            }
        });
    }

    private String getEnteredKey(String string) {
        String enteredKey =string;
        return enteredKey.replace('1', 'I').replace('0', 'O');
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getSms(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        if(MyApplication.rid==null||MyApplication.rid.equals("")){
            MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
        }
        HttpServiceClient.getInstance().sendsms(phone,"2",null).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    code.setBackgroundResource(R.drawable.yuanjiaohui);       //设置成灰色
                    code.setTextColor(getResources().getColor(R.color.white));
                    code.setEnabled(false);                     //设置不可点击
                    thread = null;
                    thread = new timeThread();
                    thread.start();
                    Log.d("dcz","获取数据成功");
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
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SmsActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
