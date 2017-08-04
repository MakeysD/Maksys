package com.example.duan.chao.DCZ_activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.StatusBarUtil;

/**
 * Created by duan on 2017/6/16.
 */

public class BaseActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setImgTransparent(this);      //这行是让标题沉浸
        Log.i("BaseActivity",getClass().getSimpleName());
        ActivityUtils.getInstance().pushActivity(this);
    }
}
