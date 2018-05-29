package com.example.duan.chao2.DCZ_util;

import android.text.TextUtils;
import android.util.Log;

import com.example.duan.chao2.DCZ_activity.HavaMoneyActivity;
import com.example.duan.chao2.DCZ_activity.HaveScanActivity;
import com.example.duan.chao2.DCZ_application.MyApplication;
import com.example.duan.chao2.DCZ_bean.HttpBean;
import com.example.duan.chao2.DCZ_bean.LoginBean;
import com.example.duan.chao2.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by DELL on 2017/7/24.
 */

public class AddUpdate implements Interceptor{
    private Gson mGson = new Gson();
    private String sign;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Log.i("Cookie3",MyApplication.sf.getString("cookie","")+"789");
        if(!TextUtils.isEmpty(MyApplication.sf.getString("cookie",""))){
            originalRequest = originalRequest.newBuilder().addHeader("cookie",MyApplication.sf.getString("cookie","")).build();
        }
        if(MyApplication.language.equals("")){  //使用系统语言
            if(MyApplication.xitong.equals("en_US")||MyApplication.xitong.equals("en_GB")){
                originalRequest = originalRequest.newBuilder().addHeader("lang","en").build();
            }else if(MyApplication.xitong.equals("th_TH")){
                originalRequest = originalRequest.newBuilder().addHeader("lang","th").build();
            }else {
                originalRequest = originalRequest.newBuilder().addHeader("lang","zh").build();
            }
        }else {     //使用设定语言
            if(MyApplication.language.equals("CHINESE")){
                originalRequest = originalRequest.newBuilder().addHeader("lang","zh").build();
            }else if(MyApplication.language.equals("ENGLISH")){
                originalRequest = originalRequest.newBuilder().addHeader("lang","en").build();
            }else {
                originalRequest = originalRequest.newBuilder().addHeader("lang","th").build();
            }
        }
        Response originalResponse = chain.proceed(originalRequest);
        String s = originalResponse.body().string();
        Log.i("结果",s+"1");
        if(s.isEmpty()){
            throw  new MyThrow();

        }
        LoginBean result=null;
        try {
            result = mGson.fromJson(s, LoginBean.class);
            if(MyApplication.map.get(result.getCode())==null){
                throw  new MyThrow();
            }
            List<String> b = originalResponse.headers("Set-Cookie");
            Log.i("Cookie1",b+"");
            for(int i=0;i<b.size();i++){
                if(i==1){
                    String[] c = b.get(i).split(";");
                    String a = c[0];
                    MyApplication.sf.edit().putString("cookie",a).commit();
                    Log.i("Cookie5",MyApplication.sf.getString("cookie","")+"5");
                }
            }
            if(result.getCode()!=null){
            /*if(result.getCode().equals("20000")){
                new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),ActivityUtils.getInstance().getCurrentActivity().getString(R.string.tishi119a),R.style.registDialog).show();
                throw  new MyThrow();
            }*/
                if(result.getCode().equals("20003")){
                    Log.i("dcz","安全中心不可用");
                    Request loginRequest = getLoginRequest();
                    Response loginResponse = chain.proceed(loginRequest);
                    String loginString = loginResponse.body().string();
                    HttpBean resultLogin = mGson.fromJson(loginString, HttpBean.class);
                    Log.i("dczz",resultLogin.getCode()+"");
                    if(resultLogin.getCode().equals("10500")){
                        Log.i("dczz","安全中心不可用");
                    }else if(resultLogin.getCode().equals("20000")) {
                        List<String> list = loginResponse.headers("Set-Cookie");
                        Log.i("dcz刷新的Cookie",list+"");
                        for(int i=0;i<list.size();i++){
                            if(i==1){
                                String[] c = list.get(i).split(";");
                                String a = c[0];
                                MyApplication.sf.edit().putString("cookie",a).commit();
                                Log.i("Cookie6",MyApplication.sf.getString("cookie","")+"5");
                            }
                        }
                        originalRequest=originalRequest.newBuilder().removeHeader("cookie").build();
                        originalRequest=originalRequest.newBuilder().addHeader("cookie",MyApplication.sf.getString("cookie","")).build();
                        return chain.proceed(originalRequest);
                    }else {
                        Log.i("dcz刷新token",resultLogin.getCode());
                        MyApplication.sms_type="0";MyApplication.sf.edit().putString("sms_type","0").commit();
                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        //下线通知
                        if(ActivityUtils.getInstance().getCurrentActivity()instanceof HaveScanActivity){
                            HaveScanActivity.mHandler2.sendEmptyMessage(1);
                        }else if(ActivityUtils.getInstance().getCurrentActivity()instanceof HavaMoneyActivity){
                            HavaMoneyActivity.mHandler2.sendEmptyMessage(1);
                        }else {
                            MainActivity.mHandler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        }catch (Exception e){
            throw  new MyThrow();
        }
        originalResponse = originalResponse.newBuilder()
                .body(ResponseBody.create(null, s))
                .build();
        return originalResponse;
    }

    private Request getLoginRequest() {
        String max= RandomUtil.RandomNumber();
        String str ="deviceUUID="+MyApplication.device+"&nonce="+max+"&refreshToken="+MyApplication.token+"&username="+MyApplication.username;
        byte[] data = str.getBytes();
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Cookie4",MyApplication.cookie+"");
        return new Request.Builder()
                .url(MyApplication.uri+"loginByRefreshToken")
                .addHeader("cookie",MyApplication.sf.getString("cookie",""))
                .post(new FormBody.Builder()
                        .add("username", MyApplication.username)
                        .add("refreshToken",MyApplication.token)
                        .add("deviceUUID",MyApplication.device)
                        .add("nonce",max)
                        .add("sign",sign)
                        .build())
                .build();
    }
    private Request Login() {
        return new Request.Builder()
                .url("http://110.79.11.5/user-safe-api/login")
                .post(new FormBody.Builder()
                        .add("username", MyApplication.username)
                        .add("password",MyApplication.password)
                        .add("deviceUUID",MyApplication.device)
                        .add("deviceName",MyApplication.xinghao)
                        .add("registrationId",MyApplication.rid)
                        .build())
                .build();
    }

    public static class MyThrow extends IOException{

    }
}
