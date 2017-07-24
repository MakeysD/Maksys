package com.example.duan.chao;


import android.os.Bundle;


import com.example.duan.chao.DCZ_activity.BaseActivity;


public class MainActivity extends BaseActivity {
    private MainActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        setViews();
        setListener();
    }


    private void setViews() {

    }

    private void setListener() {

    }


}
