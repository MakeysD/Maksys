package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.duan.chao.DCZ_adapter.CityAdapter;
import com.example.duan.chao.DCZ_bean.CityBean;
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
            content = toString(INSTANCE.getAssets().open("city.json"), "UTF-8");
            list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
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
                finish();
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list = (List<CityBean>) jsonToList(content, new TypeToken<List<CityBean>>() {});
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
        ChangePhone3Activity.content="+"+bean.getCountry_code()+" ";
        LoginActivity.code=bean.getCountry_code()+"";
        LookPasswordActivity.code=bean.getCountry_code()+"";
        ChangePhone2Activity.code=bean.getCountry_code()+"";
        finish();
    }
}
