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
import com.example.duan.chao.DCZ_bean.CountryBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      选择国家和地区
 *
 * */
public class CityListActivity extends BaseActivity  implements CityAdapter.CityCallback{
    private CityListActivity INSTANCE;
    private CityAdapter adapter;
    //  private List<CityBean> list;
    private List<CountryBean.DataBean> list;
    //  private List<CityBean> list_serch=new ArrayList<>();
    private List<CountryBean.DataBean> list_serch=new ArrayList<>();
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
        //setViews();
        getCity();
        setListener();
    }
    private void init(){
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
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getCountryName().contains(s)){
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

    public void getCity(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        HttpServiceClient.getInstance().getCountry(null).enqueue(new Callback<CountryBean>() {
            @Override
            public void onResponse(Call<CountryBean> call, Response<CountryBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        list = response.body().getData();
                        init();
                    }else {
                        new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                    }
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                }
            }
            @Override
            public void onFailure(Call<CountryBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LoginEmailActivity){
                    Log.i("dcz异常",call.toString()+"异常");
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
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
    public void addAction(CountryBean.DataBean bean) {
        ChangePhone3Activity.guo_name=bean.getCountryName();
        MyApplication.city=bean.getCountryName();
        MyApplication.code=bean.getPhoneCode()+"";
        ActivityUtils.getInstance().popActivity(INSTANCE);
    }
}
