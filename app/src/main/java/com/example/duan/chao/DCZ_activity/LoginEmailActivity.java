package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.AsterPassword;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.LocationUtils;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.duan.chao.DCZ_activity.CityListActivity.jsonToList;

public class LoginEmailActivity extends BaseActivity {
    private LoginEmailActivity INSTANCE;
    private static List<CityBean> list;
    private LoginOkBean data;
    private Dialog dialog;
    private String content;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;
    @BindView(R.id.xian2)
    TextView xian2;
    @BindView(R.id.xian3)
    TextView xian3;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;

    @BindView(R.id.button)
    TextView button;        //下一步
    @BindView(R.id.button1)
    TextView button1;       //注册
    @BindView(R.id.button2)
    TextView button2;       //忘记密码
    @BindView(R.id.et_phone)
    EditText phone;         //手机
    @BindView(R.id.et_mima)
    EditText mima;          //密码
    @BindView(R.id.checkBox)
    CheckBox yan;           //眼睛

    @BindView(R.id.ceshi)
    TextView ceshi;
    @BindView(R.id.zhengshi)
    TextView zhengshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        INSTANCE=this;
        ButterKnife.bind(this);
        JPushInterface.stopPush(getApplicationContext());
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        mima.setTransformationMethod(new AsterPassword());
        try {
            content = ShebeiUtil.ToString(INSTANCE.getAssets().open("city.json"), "UTF-8");
            list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
            Log.i("dcz",list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShebeiUtil.setEdNoChinaese(phone);
        ShebeiUtil.setEdNoChinaese(mima);
        ShebeiUtil.setEdit(phone);
        ShebeiUtil.setEdit(mima);
        LocationUtils.register(this, 10000, 10000, new LocationUtils.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                Log.i("dcz3","getLastKnownLocation");
                //获取当前位置，这里只用到了经纬度
                String string = "纬度为：" + location.getLatitude() + ",经度为："
                        + location.getLongitude();
                Log.i("dcz",string+"z");
                try {
                    getAddressFromLocation(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onLocationChanged(Location location) {
                Log.i("dcz3","onLocationChanged");
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("dcz3","onStatusChanged");
            }
        });
        setViews();
        setListener();
        ceshi();
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
        new Thread(){
            @Override
            public void run() {
                try {
                    //DSACoder.generateKeyPairs();
                    DSA.intkey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     *  监听
     * */
    private void setListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.pub_key!=null){
                    if(MyApplication.sms_type.equals("1")){
                        login();
                    }else {
                        login();
                    }
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(MyApplication.ur);
                intent.setData(content_url);
                startActivity(intent);
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
                    mima.setTransformationMethod(new AsterPassword());
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
                if(s.equals(" ")){

                }
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

        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login03);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        Intent intent=new Intent(INSTANCE, CityListActivity.class);
        startActivity(intent);
    }

    private void type2(){

        iv2.setImageResource(R.mipmap.login2);
        xian2.setBackgroundColor(Color.parseColor("#0581c6"));
        iv3.setImageResource(R.mipmap.login03);
        xian3.setBackgroundColor(Color.parseColor("#343436"));
    }

    private void type3(){

        iv2.setImageResource(R.mipmap.login02);
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        iv3.setImageResource(R.mipmap.login3);
        xian3.setBackgroundColor(Color.parseColor("#0581c6"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //关闭时解除监听器
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LocationUtils.unregister();
    }

    /**
     * 根据经纬度解码地理位置
     *
     * @param
     * @param location
     * @return
     */

    private void getAddressFromLocation(Location location) throws IOException {
        String address = LocationUtils.getCountryName(this, location.getLatitude(), location.getLongitude());
        Log.i("dcz国家",address+"z");
        if(address!=null&&!address.equals("unknown")){
        }
    }
    /***
     *  密码验证
     * */
    public void login(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog = DialogUtil.createLoadingDialog(this, getString(R.string.loaddings), "1");
        dialog.show();
        HttpServiceClient.getInstance().checklogin(phone.getText().toString()+"@qeveworld.com",DSA.md5(mima.getText().toString())).enqueue(new Callback<LoginOkBean>() {
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
                        data=response.body().getData();
                        Intent intent=new Intent(INSTANCE,SmsActivity.class);
                        intent.putExtra("phone",phone.getText().toString()+"@qeveworld.com");
                        intent.putExtra("password",DSA.md5(mima.getText().toString()));
                        startActivity(intent);
                    }else {
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
                }else {
                    new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LoginEmailActivity){
                    dialog.dismiss();
                    Log.i("dcz异常",t.getMessage()+"异常");
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
            MyApplication.rid = JPushInterface.getRegistrationID(INSTANCE);
            if(MyApplication.rid==null||MyApplication.rid.equals("")){
                Toast.makeText(INSTANCE,"RID为空",Toast.LENGTH_SHORT);
            }
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
                        intent.putExtra("type","1");
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
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LoginEmailActivity){
                    dialog.dismiss();
                    Log.i("dcz异常",call.toString()+"异常");
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    private void ceshi(){
        ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.uri.equals("http://110.79.11.5/user-safe-api/")){
                    ContentUtil.makeToast(INSTANCE,"已经是测试版");
                }else {
                    MyApplication.uri="http://110.79.11.5/user-safe-api/";MyApplication.sf.edit().putString("uri","http://110.79.11.5/user-safe-api/").commit();
                    MyApplication.ur="http://test-makeys.qeveworld.com/#/register";MyApplication.sf.edit().putString("ur","http://test-makeys.qeveworld.com/#/register").commit();
                    ContentUtil.makeToast(INSTANCE,"已切换到测试版");
                }
            }
        });
        zhengshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.uri.equals("http://api.qeveworld.com/user-safe-api/")){
                    ContentUtil.makeToast(INSTANCE,"已经是正式版");
                }else {
                    MyApplication.uri="http://api.qeveworld.com/user-safe-api/";MyApplication.sf.edit().putString("uri","http://api.qeveworld.com/user-safe-api/").commit();
                    MyApplication.ur="http://makeys.qeveworld.com/#/register";MyApplication.sf.edit().putString("ur","http://makeys.qeveworld.com/#/register").commit();
                    ContentUtil.makeToast(INSTANCE,"已切换到正式版");
                }
            }
        });
    }
}
