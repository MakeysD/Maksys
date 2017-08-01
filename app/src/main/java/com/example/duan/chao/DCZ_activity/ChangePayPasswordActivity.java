package com.example.duan.chao.DCZ_activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      修改支付密码
 *
 * */
public class ChangePayPasswordActivity extends BaseActivity {
    private ChangePayPasswordActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.button)
    TextView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_password);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
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
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>0&&et3.getText().toString().length()>0){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>0&&et3.getText().toString().length()>0){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().length()>0&&et2.getText().toString().length()>0&&et3.getText().toString().length()>0){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
