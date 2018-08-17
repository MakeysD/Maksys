package com.example.duan.chao.DCZ_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.UserStateBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
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
 *      实名信息
 *
 * */
public class PersonDataActivity extends BaseActivity {
    private PersonDataActivity INSTANCE;
    private String state="789";
    private Dialog dialog;
    private String content;     //错误原因
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.state_tv)
    TextView tv_state;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.iv)
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        state=getIntent().getStringExtra("state");
        content=getIntent().getStringExtra("content");
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        setViews();
        setListener();
        checkUserName();
    }

    private void checkUserName(){
        if (!TextUtils.isEmpty(MyApplication.webUserName)) {
            if (!MyApplication.webUserName.equals(MyApplication.username)) {
                new AlertDialog.Builder(this).setMessage(R.string.zhanghaobuyizhi)
                        .setPositiveButton(R.string.button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse(MyApplication.redirect_uri);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage(MyApplication.webPackName);
                                startActivity(intent);
                                ActivityUtils.getInstance().popAllActivities();
                            }
                        }).create().show();
                //new MiddleDialog(SettingDataActivity.this,"","当前帐号与第三方平台帐号不一致，请重新登录","",)
            }
        }
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
        if(state.equals("1")){
            state1();
        }else if(state.equals("0")){
            state0();
        }else if(state.equals("2")){
            state2();
        }else {
            state3();
        }
        if(state.equals("2")){
            tv2.setText(TextUtils.isEmpty(content)|| content.equals("null")?getString(R.string.tishi152):content);
        }else {
            if(content!=null){
                switch (content){
                    case "0":tv2.setText(R.string.tishi152);break;
                    case "1":tv2.setText(R.string.tishi153);break;
                    case "2":tv2.setText(R.string.tishi154);break;
                    case "80003":tv2.setText(R.string.code45);break;
                    case "80004":tv2.setText(R.string.code46);break;
                    case "80008":tv2.setText(R.string.code52);break;
                    case "90033":tv2.setText(R.string.code47);break;
                    case "90099":tv2.setText(R.string.code48);break;
                    default:break;
                }
            }
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("dcz","onNewIntent");
        getData();
    }

    /**
     * 待审核
     * */
    private void state0(){
        iv.setVisibility(View.GONE);
        tv_state.setText(INSTANCE.getString(R.string.tishi48a));tv_state.setTextColor(getResources().getColor(R.color.text09));
        tv2.setVisibility(View.VISIBLE);tv2.setText(R.string.tishi48e);
        //button.setVisibility(View.GONE);
        button.setText(R.string.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(MyApplication.redirect_uri)) {
                    ActivityUtils.getInstance().popActivity(INSTANCE);
                }else {
                    Uri uri = Uri.parse(MyApplication.redirect_uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage(MyApplication.webPackName);
                    startActivity(intent);
                    MyApplication.redirect_uri="";
                    ActivityUtils.getInstance().popAllActivities();
                }
            }
        });
    }
    /**
     * 已认证
     * */
    private void state1(){
        iv.setVisibility(View.VISIBLE);iv.setImageResource(R.mipmap.shenfen_ok);
        tv_state.setText(INSTANCE.getString(R.string.tishi48));tv_state.setTextColor(getResources().getColor(R.color.text09));
        tv2.setVisibility(View.VISIBLE);tv2.setText(R.string.tishi48d);
        button.setText(R.string.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(MyApplication.redirect_uri)) {
                    ActivityUtils.getInstance().popActivity(INSTANCE);
                }else {
                    Uri uri = Uri.parse(MyApplication.redirect_uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage(MyApplication.webPackName);
                    startActivity(intent);
                    MyApplication.redirect_uri="";
                    ActivityUtils.getInstance().popAllActivities();
                }
            }
        });
    }

    /**
     * 认证失败
     * */
    private void state2(){
        iv.setVisibility(View.GONE);
        tv_state.setText(INSTANCE.getString(R.string.tishi48b));tv_state.setTextColor(getResources().getColor(R.color.text_red));
        tv2.setVisibility(View.VISIBLE);tv2.setText(R.string.tishi48f);
        button.setVisibility(View.VISIBLE);button.setText(R.string.tishi48h);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,SettingDataActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 没有认证信息
     * */
    private void state3(){
        iv.setVisibility(View.VISIBLE);iv.setImageResource(R.mipmap.shenfen_no);
        tv_state.setText(INSTANCE.getString(R.string.tishi47));tv_state.setTextColor(getResources().getColor(R.color.white));
        tv2.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);button.setText(R.string.tishi46);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,SettingDataActivity.class);
                startActivity(intent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog.show();
        HttpServiceClient.getInstance().userState(null).enqueue(new Callback<UserStateBean>() {
            @Override
            public void onResponse(Call<UserStateBean> call, Response<UserStateBean> response) {
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
                            Log.i("dcz_code",response.body().getData().getCode()+"z");
                            state=response.body().getData().getCode();
                            content=response.body().getData().getDescription()+"";
                            Log.i("dcz",content+"123");
                            setViews();
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
            public void onFailure(Call<UserStateBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SettingDataActivity){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
