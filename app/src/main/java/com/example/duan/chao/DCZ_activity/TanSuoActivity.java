package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;
import android.view.View;

import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TanSuoActivity extends BaseActivity {
    private TanSuoActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tan_suo);
        INSTANCE=this;
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
    }
}
