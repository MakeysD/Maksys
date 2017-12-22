package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.duan.chao.DCZ_adapter.CityAdapter;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      选择国家和地区
 *
 * */
public class CityListActivity extends BaseActivity  implements CityAdapter.CityCallback{
    private String phone="18788888888";
    private CityListActivity INSTANCE;
    private CityAdapter adapter;
    private List<CityBean> list;
    private List<CityBean> list_serch=new ArrayList<>();
    private String content;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.et)
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        INSTANCE=this;
        setViews();
        setListener();
    }

    private void setViews(){
        try {
            if(ContentUtil.isMobileNO(MyApplication.mobile)){//中国手机
                List<CityBean> zz=new ArrayList<>();
                CityBean bean=new CityBean();
                bean.setCountry_id(100042);bean.setCountry_code(86);bean.setCountry_name_cn("中国");bean.setCountry_name_en("China");bean.setAb("CN");
                zz.add(bean);
                list=zz;
            }else {
                content = toString(INSTANCE.getAssets().open("city.json"), "UTF-8");
                list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
            }
            Log.i("dcz",list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter=new CityAdapter(INSTANCE,list,this);
        lv.setAdapter(adapter);
    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("dcz",list.toString());
                if(list_serch!=null){
                    list_serch.clear();
                }
                int length=s.length();
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getCountry_name_cn().contains(s)){
                       list_serch.add(list.get(i));
                    }
                }
                adapter.notify(list_serch);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public static String toString(InputStream is, String charset) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }

    /**
     * Json转对象
     */
    public static Object jsonToObject(String json, Type type) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(json, type);
    }

    /**
     * json解析回ArrayList,参数为new TypeToken<ArrayList<T>>() {},必须加泛型
     */
    public static List<?> jsonToList(String json, TypeToken<?> token) {
        return (List<?>) jsonToObject(json, token.getType());
    }

    @Override
    public void addAction(CityBean bean) {
        if(MyApplication.language.equals("ENGLISH")){
            ChangePhone3Activity.guo_name=bean.getCountry_name_en();
            MyApplication.city=bean.getCountry_name_en();
            MyApplication.code=bean.getCountry_code()+"";
        }else {
            ChangePhone3Activity.guo_name=bean.getCountry_name_cn();
            MyApplication.city=bean.getCountry_name_cn();
            MyApplication.code=bean.getCountry_code()+"";
        }
        ActivityUtils.getInstance().popActivity(INSTANCE);
    }
}
