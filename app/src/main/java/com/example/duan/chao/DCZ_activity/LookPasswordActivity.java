package com.example.duan.chao.DCZ_activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.SwitchButton;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *      找回密码
 *
 * */
public class LookPasswordActivity extends BaseActivity {
    private LookPasswordActivity INSTANCE;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.quhao)
    EditText quhao;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_mima)
    EditText et_mima;
    @BindView(R.id.et_mima2)
    EditText et_mima2;
    @BindView(R.id.select1)
    SwitchButton select1;
    @BindView(R.id.select2)
    SwitchButton select2;
    @BindView(R.id.button)
    TextView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_password);
        INSTANCE=this;
        ButterKnife.bind(this);
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
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
        //密码开关
        select1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_mima.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //替换成*号
                    et_mima.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
            }
        });
        //确认密码开关
        select2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_mima2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //替换成*号
                    et_mima2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '*';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };
}
