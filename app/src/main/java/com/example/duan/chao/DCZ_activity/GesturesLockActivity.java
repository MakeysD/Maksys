package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_lockdemo.CustomLockView;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_lockdemo.ScreenObserver;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *     手势锁
 *
 * */
public class GesturesLockActivity extends BaseActivity {
    public GesturesLockActivity context;
    public <K extends View> K getViewById(int id) {
        return (K) getWindow().findViewById(id);
    }
    private ScreenObserver mScreenObserver;
    private ImageView iva,ivb,ivc,ivd,ive,ivf,ivg,ivh,ivi;
    private List<ImageView> list=new ArrayList<ImageView>();
    private TextView tvWarn;
    private int times=0;
    private int[] mIndexs=null;
    private String type;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures_lock);
        ButterKnife.bind(this);
        type=getIntent().getStringExtra("type");
        context=this;
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenStateChange(boolean isScreenOn) {
                if (!isScreenOn&& LockUtil.getPwdStatus(context)&& LockUtil.getPwd(context).length>0) {
                    //invalidateUser( );
                }
            }
        });
        initView();

        final CustomLockView cl=(CustomLockView)findViewById(R.id.cl);
        cl.hui(new CustomLockView.OnType(){
            @Override
            public void ontate() {
                tvWarn.setText(R.string.lock12);
                tvWarn.setTextColor(getResources().getColor(R.color.red));
                tvWarn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
            }
        });
        cl.setOnCompleteListener(new CustomLockView.OnCompleteListener() {
            @Override
            public void onComplete(int[] indexs) {
                mIndexs=indexs;
                //显示次数
                if(times==0){
                    for(int i=0;i<indexs.length;i++){
                        list.get(indexs[i]).setImageDrawable(getResources().getDrawable(R.mipmap.gesturecirlebrownsmall));
                    }
                    tvWarn.setText(R.string.lock1);
                    tvWarn.setTextColor(getResources().getColor(R.color.text05));
                    times++;
                    Log.i("dcz",mIndexs.length+"");
                }else if(times==1){
                    //将密码设置在本地
                    LockUtil.setPwdToDisk(context, mIndexs);
                    LockUtil.setPwdStatus(context, true);
                    //会员验证
                    invalidateUser();
                }
            }

            @Override
            public void onError() {
                tvWarn.setText(R.string.lock2);
                tvWarn.setTextColor(getResources().getColor(R.color.red));
            }
        });

    }

    @Override
    protected void onDestroy() {
        if(mScreenObserver!=null){
            mScreenObserver.stopScreenStateUpdate();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        //初始化9个小圆
        iva=(ImageView)findViewById(R.id.iva);
        ivb=(ImageView)findViewById(R.id.ivb);
        ivc=(ImageView)findViewById(R.id.ivc);
        ivd=(ImageView)findViewById(R.id.ivd);
        ive=(ImageView)findViewById(R.id.ive);
        ivf=(ImageView)findViewById(R.id.ivf);
        ivg=(ImageView)findViewById(R.id.ivg);
        ivh=(ImageView)findViewById(R.id.ivh);
        ivi=(ImageView)findViewById(R.id.ivi);
        list.add(iva);
        list.add(ivb);
        list.add(ivc);
        list.add(ivd);
        list.add(ive);
        list.add(ivf);
        list.add(ivg);
        list.add(ivh);
        list.add(ivi);
        tvWarn=getViewById(R.id.tvWarn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(context);
            }
        });
    }

    /**
     * 会员验证
     */
    private void invalidateUser( ){
      if(type!=null){
          Activity ac = ActivityUtils.getInstance().getActivity(ActivityUtils.getInstance().ActivitySize() - 2);
          ActivityUtils.getInstance().popActivity(ac);
      }else {
          Activity ac = ActivityUtils.getInstance().getActivity(ActivityUtils.getInstance().ActivitySize() - 1);
          ActivityUtils.getInstance().popActivity(ac);
      }
        ActivityUtils.getInstance().popActivity(this);
    }
}
