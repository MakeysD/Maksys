package com.example.duan.chao.DCZ_activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.AuthorBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.BService;
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

public class AuthorActivity extends BaseActivity{
    private AuthorActivity INSTANCE;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(ok).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(no).rippleCorner(MyApplication.dp2Px()).create();
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
                send(null);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(null);
            }
        });
    }

    public void ceshi(){
        String max= RandomUtil.RandomNumber();
        String str ="client_id="+BService.App_key+
                "&nonce="+max+
                "&redirect_uri="+BService.redirect_uri/*+
                "&scope="+ "a"+
                "&state="+"a"*/;
        Log.i("str",str);
        byte[] data = str.getBytes();
        String sign=null;
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> map=new HashMap<>();
        map.put("client_id",BService.App_key);
        map.put("nonce",max);
        map.put("redirect_uri",BService.redirect_uri);
        map.put("sign",sign);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
        HttpServiceClient.getInstance().author(body).enqueue(new Callback<AuthorBean>() {
            @Override
            public void onResponse(Call<AuthorBean> call, Response<AuthorBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        send(response.body());
                    }else {
                        Toast.makeText(INSTANCE,"失败", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(INSTANCE,"尚未登录设备", Toast.LENGTH_SHORT).show();
                    Log.d("dcz_数据获取失败",response.message());
                }
            }
            @Override
            public void onFailure(Call<AuthorBean> call, Throwable t) {
                Log.i("dcz异常",call.toString());
                Toast.makeText(INSTANCE, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void send(AuthorBean bean){
        Bundle bundle=null;
        try {
            if(BService.message==null||BService.to==null){
            }else {
                bundle = new Bundle();
                bundle.putString("type","1");  //传1对方会有回调消息，0则没有
                if(bean!=null){
                    Gson mGson = new Gson();
                    bundle.putString("type","1");
                    bundle.putString("json",mGson.toJson(bean));
                    Log.i("json对象",mGson.toJson(bean));
                }else {
                    bundle.putString("json",null);
                }
                BService.message.setData(bundle);
                BService.to.send(BService.message);
                BService.App_key=null;
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        //判断APP是否在前台
        if(ActivityUtils.getInstance().isAppOnForeground(this)==false) {
            Log.i("dcz","APP已进入后台");
            ActivityUtils.getInstance().popActivity(INSTANCE);
        }
    }
}
