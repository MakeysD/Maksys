package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_bean.OperationRecordBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.DisplayUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HaveActivity extends BaseActivity {
    private HaveActivity INSTANCE;
    private Dialog dialog;
    private Handler handler = null;
    private String authzId;
    private Timer timer;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.anima)
    RelativeLayout anima;
    @BindView(R.id.tv)
    TextView tv;

    @BindView(R.id.gif_ok)
    GifImageView gif;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.yuan)
    RelativeLayout yuan;
    @BindView(R.id.diannao)
    LinearLayout diannao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(ok).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(no).rippleCorner(MyApplication.dp2Px()).create();
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
        newhandler();
        if(MyApplication.reqSysId!=null){
            if(MyApplication.language.equals("CHINESE")){
                textView7.setTextSize(16);
                textView7.setText(MyApplication.map.get(MyApplication.reqSysId)+this.getString(R.string.tishi112));
                type.setTextSize(18);
            }else {
                type.setTextSize(15);
                textView7.setTextSize(14);
                textView7.setText(MyApplication.map.get(MyApplication.reqSysId)+" "+this.getString(R.string.tishi112));
            }
        }
        timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what=3;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task,30000);
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
        anima.setEnabled(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                JPushInterface.clearAllNotifications(INSTANCE);
                String str ="agreement=1"+"&reqFlowId="+MyApplication.reqFlowId+"&reqSysId=2001"+"&srcReqSysId="+MyApplication.reqSysId+"&username="+MyApplication.username;
                byte[] data = str.getBytes();
                try {
                    String sign = DSA.sign(data, MyApplication.pri_key);
                    getData("1",sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                JPushInterface.clearAllNotifications(INSTANCE);
                String str ="agreement=2"+"&reqFlowId="+MyApplication.reqFlowId+"&reqSysId=2001"+"&srcReqSysId="+MyApplication.reqSysId+"&username="+MyApplication.username;
                byte[] data = str.getBytes();
                try {
                    String sign = DSA.sign(data, MyApplication.pri_key);
                    getData("2",sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(final String string, String sign){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().have(MyApplication.username,"2001",MyApplication.reqSysId,MyApplication.reqFlowId,string,sign).enqueue(new Callback<LoginOkBean>() {
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
                            if(string.equals("1")){
                                yuan.setBackgroundResource(R.drawable.yuan_lv);
                                diannao.setBackgroundResource(R.drawable.diannao_lv);
                                type.setText(R.string.tishi138);
                                anima.setVisibility(View.VISIBLE);
                                timer("1",response.body().getDesc());
                            }else {
                                error("2");
                            }
                        }else {
                            if(response.body().getCode().equals("10516")){
                                MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                                new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                                    @Override
                                    public void onActivieButtonClick(Object bean, int position) {
                                        ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                        ActivityUtils.getInstance().popAllActivities();
                                    }
                                }, R.style.registDialog).show();
                            }else{
                                error("2");
                            }
                        }
                    }else {
                        error("2");
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    error("2");
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof HaveActivity){
                    error("2");
                }
            }
        });
    }
    private void error(String string){
        yuan.setBackgroundResource(R.drawable.yuan_red);
        diannao.setBackgroundResource(R.drawable.diannao_hong);
        type.setText(R.string.tishi139);
        anima.setVisibility(View.VISIBLE);
        timer(string,"");
    }

    private void timer(final String string, final String code){
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                if(string.equals("1")){
                    msg.what=1;
                    handler.sendMessage(msg);
                }else {
                    msg.what=0;
                    handler.sendMessage(msg);
                }
                timer2();
            }
        };
        timer.schedule(task,800);
    }
    private void timer2(){
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        };
        timer.schedule(task,2000);
    }

    private void newhandler() {
        handler = new Handler(INSTANCE.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    gif.setImageResource(R.drawable.progress);
                    AnimationDrawable animationDrawable = (AnimationDrawable) gif.getDrawable();
                    animationDrawable.start();
                }else if(msg.what==3){
                    yuan.setBackgroundResource(R.drawable.yuan_red);
                    diannao.setBackgroundResource(R.drawable.diannao_hong);
                    type.setText(R.string.tishi139a);
                    anima.setVisibility(View.VISIBLE);
                    timer("2","");
                } else {
                    gif.setImageResource(R.drawable.progress2);
                    AnimationDrawable animationDrawable = (AnimationDrawable) gif.getDrawable();
                    animationDrawable.start();
                }
            }
        };
    }
}
