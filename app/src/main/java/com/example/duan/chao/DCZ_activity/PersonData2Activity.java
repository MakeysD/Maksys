package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.StrBean;
import com.example.duan.chao.DCZ_bean.UserStateBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  台湾实名
 * */
public class PersonData2Activity extends BaseActivity {
    private PersonData2Activity INSTANCE;
    private String state="3";//0:信用卡认证中、1：信用卡认证成功、2：信用卡认证失败、3：待提交信用卡信息
    private String content;     //错误原因
    @BindView(R.id.back)
    View back;
    @BindView(R.id.ll_state)
    LinearLayout ll_state;
    @BindView(R.id.ll_y)
    LinearLayout ll_y;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.state_tv)
    TextView state_tv;
    @BindView(R.id.content)
    TextView tv2;
    @BindView(R.id.button2)
    TextView button2;       //认证信用卡
    @BindView(R.id.button)
    TextView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data2);
        INSTANCE=this;
        ButterKnife.bind(this);
        state=getIntent().getStringExtra("state");
        Log.i("dcz",state+"z");
        content=getIntent().getStringExtra("content");
        setViews();
        setListener();
    }

    private void setViews() {
        switch (state){
            case "0":state0();break;
            case "1":state1();break;
            case "2":state2();break;
            case "3":state3();break;
        }
    }
/*    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("dcz","onNewIntent");
        getData();
    }*/

    /**
     * 信用卡认证中
     * */
    private void state0(){
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.mipmap.tw);
        ll_y.setVisibility(View.GONE);
        ll_state.setVisibility(View.VISIBLE);
        state_tv.setText(INSTANCE.getString(R.string.tishi180));
        state_tv.setTextColor(getResources().getColor(R.color.text09));
        tv2.setText(R.string.tishi48e);
        button.setText(R.string.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
    }
    /**
     * 信用卡认证成功
     * */
    private void state1(){
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.mipmap.tw_ok);
        ll_y.setVisibility(View.GONE);
        ll_state.setVisibility(View.VISIBLE);
        state_tv.setText(R.string.tishi165);
        state_tv.setTextColor(getResources().getColor(R.color.text09));
        tv2.setText(INSTANCE.getString(R.string.tishi166)+content);
        button.setText(R.string.tishi167);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE,SettingData2Activity.class);
                startActivity(intent);
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
    }

    /**
     * 信用卡认证失败
     * */
    private void state2(){
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.mipmap.tw_no);
        ll_y.setVisibility(View.GONE);
        ll_state.setVisibility(View.VISIBLE);
        state_tv.setText(R.string.tishi169);
        state_tv.setTextColor(getResources().getColor(R.color.text_red));
        tv2.setText(INSTANCE.getString(R.string.tishi168)+content);
        button.setVisibility(View.VISIBLE);button.setText(R.string.tishi48h);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSeria();
            }
        });
    }
    /**
     * 待提交信用卡信息
     * */
    private void state3(){
        ll_y.setVisibility(View.VISIBLE);
        ll_state.setVisibility(View.GONE);
        state_tv.setTextColor(getResources().getColor(R.color.white));
    }

    private void setListener() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSeria();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        HttpServiceClient.getInstance().userState(null).enqueue(new Callback<UserStateBean>() {
            @Override
            public void onResponse(Call<UserStateBean> call, Response<UserStateBean> response) {
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
                            Log.i("dcz_code",response.body().getData().getStep()+"z");
                            state=response.body().getData().getCode()+"";
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

    /***
     * 调取接口拿到服务器数据
     * */
    public void getSeria(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        HttpServiceClient.getInstance().getSerialNum(null).enqueue(new Callback<StrBean>() {
            @Override
            public void onResponse(Call<StrBean> call, Response<StrBean> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Intent intent= new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Log.i("url","https://www.quicklylinking.com/HPPET/cart_check_request.php?serialNum="+response.body().getData());
                            Uri content_url = Uri.parse("https://www.quicklylinking.com/HPPET/cart_check_request.php?serialNum="+response.body().getData());
                            intent.setData(content_url);
                            startActivity(intent);
                        }else {
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<StrBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SettingDataActivity){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
