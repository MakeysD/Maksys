package com.example.duan.chao.DCZ_activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.CodeUtil;
import com.example.duan.chao.R;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.duan.chao.DCZ_activity.CityListActivity.jsonToList;

/**
 * 语言切换
 * */
public class LanguageActivity extends BaseActivity {
    private LanguageActivity INSTANCE;
    private String content;
    private static List<CityBean> list;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.rl1)     //简体中文
    RelativeLayout rl1;
    @BindView(R.id.rl2)     //繁体中文
    RelativeLayout rl2;
    @BindView(R.id.rl3)     //英语
    RelativeLayout rl3;
    @BindView(R.id.rl4)     //泰语
    RelativeLayout rl4;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl4).rippleCorner(MyApplication.dp2Px()).create();
        try {
            content = ActivityUtils.getInstance().ToString(INSTANCE.getAssets().open("city.json"), "UTF-8");
            list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
            Log.i("dcz",list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(MyApplication.language.equals("TAI")){
            a4();
        }else if(MyApplication.language.equals("ENGLISH")){
            a3();
        }else if(MyApplication.language.equals("")){
            if(MyApplication.xitong.equals("en_US")||MyApplication.xitong.equals("en_GB")){
                a3();
            }else if(MyApplication.xitong.equals("th_TH")){
                a4();
            }else {
                a1();
            }
        }else {
            a1();
        }
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

    }

    /**
     *  监听
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(ActivityUtils.getInstance().getCurrentActivity());
            }
        });
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1();
                MyApplication.type=1;
                MyApplication.language="CHINESE";MyApplication.sf.edit().putString("language","CHINESE").commit();
                MyApplication.status=true;
                recreate();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a2();
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a3();
                MyApplication.type=1;
                MyApplication.language="ENGLISH";MyApplication.sf.edit().putString("language","ENGLISH").commit();
                recreate();//刷新页面
                MyApplication.status=true;
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a4();
                MyApplication.type=1;
                MyApplication.language="TAI";MyApplication.sf.edit().putString("language","TAI").commit();
                recreate();//刷新页面
                MyApplication.status=true;
            }
        });
    }

    private void a1(){
        rl1.setBackgroundColor(getResources().getColor(R.color.bg2));
        rl2.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl3.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl4.setBackgroundColor(getResources().getColor(R.color.bg1));
        tv1.setTextColor(getResources().getColor(R.color.white));
        tv2.setTextColor(getResources().getColor(R.color.text07));
        tv3.setTextColor(getResources().getColor(R.color.text07));
        tv4.setTextColor(getResources().getColor(R.color.text07));
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
    }
    private void a2(){
        rl1.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl2.setBackgroundColor(getResources().getColor(R.color.bg2));
        rl3.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl4.setBackgroundColor(getResources().getColor(R.color.bg1));
        tv1.setTextColor(getResources().getColor(R.color.text07));
        tv2.setTextColor(getResources().getColor(R.color.white));
        tv3.setTextColor(getResources().getColor(R.color.text07));
        tv4.setTextColor(getResources().getColor(R.color.text07));
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.VISIBLE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
    }
    private void a3(){
        rl1.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl2.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl3.setBackgroundColor(getResources().getColor(R.color.bg2));
        rl4.setBackgroundColor(getResources().getColor(R.color.bg1));
        tv1.setTextColor(getResources().getColor(R.color.text07));
        tv2.setTextColor(getResources().getColor(R.color.text07));
        tv3.setTextColor(getResources().getColor(R.color.white));
        tv4.setTextColor(getResources().getColor(R.color.text07));
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.VISIBLE);
        iv4.setVisibility(View.GONE);
        Log.i("dcz_数量",list.size()+"");
    }
    private void a4(){
        rl1.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl2.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl3.setBackgroundColor(getResources().getColor(R.color.bg1));
        rl4.setBackgroundColor(getResources().getColor(R.color.bg2));
        tv1.setTextColor(getResources().getColor(R.color.text07));
        tv2.setTextColor(getResources().getColor(R.color.text07));
        tv3.setTextColor(getResources().getColor(R.color.text07));
        tv4.setTextColor(getResources().getColor(R.color.white));
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.VISIBLE);
    }
}
