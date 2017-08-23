package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_bean.OperationRecordBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HaveActivity extends BaseActivity {
    private HaveActivity INSTANCE;
    private Dialog dialog;
    private Handler handler = null;
    private String authzId;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.anima)
    RelativeLayout anima;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.iv5)
    ImageView iv5;
    @BindView(R.id.iv6)
    ImageView iv6;
    @BindView(R.id.tv)
    TextView tv;
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

    /**
     *  初始化
     * */
    private void setViews() {
        newhandler();
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
        anima.setEnabled(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str ="agreement=1"+"&reqFlowId="+MyApplication.reqFlowId+"&reqSysId=2001"+"&srcReqSysId="+MyApplication.reqSysId+"&username="+MyApplication.username;
                byte[] data = str.getBytes();
                try {
                    String sign = DSA.sign(data, MyApplication.private_key);
                    getData("1",sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str ="agreement=2"+"&reqFlowId="+MyApplication.reqFlowId+"&reqSysId=2001"+"&srcReqSysId="+MyApplication.reqSysId+"&username="+MyApplication.username;
                byte[] data = str.getBytes();
                try {
                    String sign = DSA.sign(data, MyApplication.private_key);
                    getData("2",sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void start(){
        setAnimation(R.anim.rotate,iv1);
        setAnimation(R.anim.rotate2,iv2);
        setAnimation(R.anim.rotate,iv3);
        setAnimation(R.anim.rotate2,iv4);
        setAnimaStop(R.anim.rotate,iv5);
    }
    private void stop(){
        iv1.clearAnimation();
        iv2.clearAnimation();
        iv3.clearAnimation();
        iv4.clearAnimation();
        iv5.clearAnimation();
    }
    private void setAnimation(int id,View iv){
        Animation operatingAnim = AnimationUtils.loadAnimation(INSTANCE, id);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            iv.startAnimation(operatingAnim);
        }
    }
    private void setAnimaStop(int id,View iv){
        Animation operatingAnim = AnimationUtils.loadAnimation(INSTANCE, id);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            iv.startAnimation(operatingAnim);
        }
    }
    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(final String string, String sign){
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().have(MyApplication.username,"2001",MyApplication.reqSysId,MyApplication.reqFlowId,string,sign).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            if(string.equals("1")){
                                anima.setVisibility(View.VISIBLE);
                                timer("1",response.body().getDesc());
                            }else {
                                //timer("2",response.body().getDesc());
                                ActivityUtils.getInstance().popActivity(INSTANCE);
                            }
                        }else {
                            timer("2",response.body().getDesc());
                            ActivityUtils.getInstance().popActivity(INSTANCE);
                        }
                    }else {
                        timer("2",response.body().getDesc());
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    timer("2","获取数据失败");
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                dialog.dismiss();
                timer("2","服务器异常");
            }
        });
    }

    private void timer(final String string, final String code){
        start();
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
                stop();
                if(msg.what==1){
                    iv6.setImageResource(R.drawable.login_ok);
                    //Toast.makeText(INSTANCE,"成功", Toast.LENGTH_SHORT).show();
                }else {
                   // Toast.makeText(INSTANCE,"失败", Toast.LENGTH_SHORT).show();
                    iv6.setImageResource(R.drawable.login_no);
                }
            }
        };
    }
}
