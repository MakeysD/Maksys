package com.example.duan.chao.DCZ_activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.AuthorBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebAuthorActivity extends BaseActivity {
    private WebAuthorActivity INSTANCE;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.textView7)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_author);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(ok).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(no).rippleCorner(MyApplication.dp2Px()).create();
        tv.setText(MyApplication.App_name+getString(R.string.tishi181));
        setListener();
    }

    private void setListener() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ceshi();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().popAllActivities();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().popAllActivities();
            }
        });
    }

    public void ceshi(){
        String max= RandomUtil.RandomNumber();
        String str=null;
        if(MyApplication.scope==null&&MyApplication.state==null){
            str ="client_id="+MyApplication.App_key+ "&nonce="+max+ "&redirect_uri="+MyApplication.redirect_uri;
        }else {
            if(MyApplication.scope==null){
                str ="client_id="+MyApplication.App_key+ "&nonce="+max+ "&redirect_uri="+MyApplication.redirect_uri+"&state="+MyApplication.state;
            }else if(MyApplication.state==null){
                str ="client_id="+MyApplication.App_key+ "&nonce="+max+ "&redirect_uri="+MyApplication.redirect_uri+ "&scope="+MyApplication.scope;
            }else {
                str ="client_id="+MyApplication.App_key+ "&nonce="+max+ "&redirect_uri="+MyApplication.redirect_uri+ "&scope="+MyApplication.scope+ "&state="+MyApplication.state;
            }
        }
        Log.i("str",str);
        byte[] data = str.getBytes();
        String sign=null;
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> map=new HashMap<>();
        map.put("client_id",MyApplication.App_key);
        map.put("nonce",max);
        map.put("redirect_uri",MyApplication.redirect_uri);
        map.put("scope",MyApplication.scope);map.put("state",MyApplication.state);
        map.put("sign",sign);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        HttpServiceClient.getInstance().author(body).enqueue(new Callback<AuthorBean>() {
            @Override
            public void onResponse(Call<AuthorBean> call, Response<AuthorBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    ActivityUtils.getInstance().popAllActivities();
                }else {
                    Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
                    Log.d("dcz_数据获取失败",response.message());
                }
            }
            @Override
            public void onFailure(Call<AuthorBean> call, Throwable t) {
                Log.i("dcz异常",call.toString());
                Toast.makeText(INSTANCE,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //判断APP是否在前台
        if(ActivityUtils.getInstance().isAppOnForeground(this)==false) {
            Log.i("dcz","APP已进入后台");
            MyApplication.Webkey=null;
            ActivityUtils.getInstance().popAllActivities();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.Webkey=null;
    }
}
