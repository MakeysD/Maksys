package com.example.duan.chao.DCZ_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.duan.chao.DCZ_adapter.Footprints1Adapter;
import com.example.duan.chao.DCZ_adapter.Footprints2Adapter;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_selft.GridViewForScrollView;
import com.example.duan.chao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      足迹
 *
 * */
public class FootprintsActivity extends BaseActivity {
    private FootprintsActivity INSTANCE;
    private Footprints1Adapter adapter1;
    private Footprints2Adapter adapter2;
    private List<FootprintsBean> list=new ArrayList<>();
    @BindView(R.id.back)
    View back;
    @BindView(R.id.lv1)
    GridViewForScrollView lv1;
    @BindView(R.id.lv2)
    GridViewForScrollView lv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprints);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        adapter1=new Footprints1Adapter(INSTANCE,list);
        lv1.setAdapter(adapter1);
        adapter2=new Footprints2Adapter(INSTANCE,list);
        lv2.setAdapter(adapter2);
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
    }
}
