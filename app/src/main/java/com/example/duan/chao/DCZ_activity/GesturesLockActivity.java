package com.example.duan.chao.DCZ_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    public Context context;
    public <K extends View> K getViewById(int id) {
        return (K) getWindow().findViewById(id);
    }
    private ScreenObserver mScreenObserver;
    private ImageView iva,ivb,ivc,ivd,ive,ivf,ivg,ivh,ivi;
    private List<ImageView> list=new ArrayList<ImageView>();
    private TextView tvWarn;
    private int times=0;
    private int[] mIndexs=null;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures_lock);
        ButterKnife.bind(this);
        context=this;
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenStateChange(boolean isScreenOn) {
                if (!isScreenOn&& LockUtil.getPwdStatus(context)&& LockUtil.getPwd(context).length>0) {
                    doSomethingOnScreenOff();
                }
            }
        });
        initView();

        final CustomLockView cl=(CustomLockView)findViewById(R.id.cl);
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

    /**
     * 打开验证手势
     */
    private void doSomethingOnScreenOff() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), LoginLockActivity.class);
        intent.putExtra("current","resume");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
                finish();
            }
        });
    }

    /**
     * 会员验证
     */
    private void invalidateUser( ){
      /*  Intent i=new Intent();
        i.setClass(this,LoginLockActivity.class);
        startActivity(i);*/
        ActivityUtils.getInstance().popAllActivities();
        finish();
    }
}
