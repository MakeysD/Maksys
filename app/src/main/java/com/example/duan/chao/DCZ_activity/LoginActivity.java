package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
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
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DSACoder;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
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
    private static List<CityBean> list;
    private LoginOkBean data;
    private Dialog dialog;
    private String content;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;
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
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        mima.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        try {
            content = ToString(INSTANCE.getAssets().open("city.json"), "UTF-8");
            list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
            Log.i("dcz",list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShebeiUtil.setEdNoChinaese(mima);

        GPS();
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
        try {
            if(MyApplication.pub_key.equals("")||MyApplication.pub_key==null){
                DSA.intkey();
               // DSACoder.initKey();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        guo.setFocusable(false);

    }
    /**
     *  监听
     * */
    private void setListener() {
        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.sms_type.equals("1")){
                    login();
                }else {
                    login();
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LookPassword2Activity.class);
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
                    if(phone.getText().toString().length()>0&&mima.getText().toString().length()>0){
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
                    if(mima.getText().toString().length()>0&&phone.getText().toString().length()>0){
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
        quhao.setText(MyApplication.code);
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

    private void GPS() {
        //获取定位服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);
        if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
            Log.i("dcz","网络位置控制器");
        } else if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            Log.i("dcz","GPS位置控制器");
            provider = LocationManager.GPS_PROVIDER;
        } else {
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi71),R.style.registDialog).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location==null){
            Log.i("dcz","location是空的");
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi71),R.style.registDialog).show();
        }else {
            Log.i("dcz",location.toString());
        }
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string = "纬度为：" + location.getLatitude() + ",经度为："
                    + location.getLongitude();
            Log.i("dcz",string);
            try {
                Log.i("dcz",getAddressFromLocation(this,location));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //绑定定位事件，监听位置是否改变
        //第一个参数为控制器类型第二个参数为监听位置变化的时间间隔（单位：毫秒）
        //第三个参数为位置变化的间隔（单位：米）第四个参数为位置监听器
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 2000, 2, locationListener);
    }
    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 更新当前经纬度
        }
    };
    //关闭时解除监听器
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
    /**
     * 根据经纬度解码地理位置
     *
     * @param activity
     * @param location
     * @return
     */

    private static String getAddressFromLocation(final Activity activity, Location location) throws IOException {
        Geocoder geocoder = new Geocoder(activity);
        try {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("dcz", "getAddressFromLocation->lat:" + latitude + ", long:" + longitude);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                //返回当前位置，精度可调
                Address address = addresses.get(0);
                Log.i("dcz1",address.getCountryName());
                MyApplication.city=address.getCountryName();MyApplication.sf.edit().putString("city",address.getCountryName()).commit();
                for(int i=0;i<list.size();i++){
                    if(MyApplication.city.equals(String.valueOf(list.get(i).getCountry_name_cn()))){
                        Log.i("dcz",list.get(i).getCountry_name_cn());
                        Log.i("dcz",list.get(i).getCountry_code()+"");
                        MyApplication.code=list.get(i).getCountry_code()+"";
                    }
                }
             /*   Log.i("dcz2",address.getFeatureName());
                Log.i("dcz3",address.getSubLocality());
                Log.i("dcz4",address.getAdminArea());*/
                address.getCountryName();
                String sAddress;
                if (!TextUtils.isEmpty(address.getLocality())) {
                    sAddress = address.getLocality();
                } else {
                    sAddress = "定位失败";
                }
                return sAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /***
     *  密码验证
     * */
    public void login(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().checklogin(phone.getText().toString(),DSA.md5(mima.getText().toString())).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        data=response.body().getData();
                        Intent intent=new Intent(INSTANCE,SmsActivity.class);
                        intent.putExtra("phone",phone.getText().toString());
                        intent.putExtra("password",mima.getText().toString());
                        startActivity(intent);
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
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LoginActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    /***
     *  登录
     * */
    public void login2(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        if(MyApplication.rid==null||MyApplication.rid.equals("")){
            MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
        }
        HttpServiceClient.getInstance().login(MyApplication.username,mima.getText().toString(),null,MyApplication.pub_key,MyApplication.device,MyApplication.xinghao,MyApplication.rid).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
                        data=response.body().getData();
                        MyApplication.token=data.getRefreshToken();MyApplication.sf.edit().putString("token",data.getRefreshToken()).commit();
                        MyApplication.nickname=data.getNickname();MyApplication.sf.edit().putString("nickname",data.getNickname()).commit();
                        MyApplication.username=data.getUsername();MyApplication.sf.edit().putString("username",data.getUsername()).commit();
                        Intent intent=new Intent(INSTANCE,MainActivity.class);
                        startActivity(intent);
                        ActivityUtils.getInstance().popActivity(INSTANCE);
                    }else {
                        new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                    }
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LoginActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
