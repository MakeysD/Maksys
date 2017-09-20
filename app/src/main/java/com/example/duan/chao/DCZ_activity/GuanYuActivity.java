package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chiclam.android.updater.Updater;
import com.chiclam.android.updater.UpdaterConfig;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_bean.VersionBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *     关于
 *
 * */
public class GuanYuActivity extends BaseActivity {
    private GuanYuActivity INSTANCE;
    private Dialog dialog;
    private String version;
    private String path="";
    @BindView(R.id.back)
    View back;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guan_yu);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        // 获取packagemanager的实例  
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息  
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packInfo.versionName;
        tv.setText(this.getString(R.string.tishi70)+" v"+version);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVersion();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /***
     *  验证版本
     * */
    public void getVersion(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().version(MyApplication.device,"android",version,"1").enqueue(new Callback<VersionBean>() {
            @Override
            public void onResponse(Call<VersionBean> call, Response<VersionBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        if(response.body().getData().getLatestVersion()!=null){
                            if(response.body().getData().getLatestVersion().compareTo(version)==1){
                                path=response.body().getData().getPath().toString();
                                //强制更新版本
                                if(response.body().getData().getNeededUpdated().equals("1")){
                                    UpdaterConfig config = new UpdaterConfig.Builder(INSTANCE)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setDescription(getString(R.string.system_download_description))
                                            .setFileUrl(response.body().getData().getPath()+"")
                                            .setCanMediaScanner(true)
                                            .build();
                                    Updater.get().showLog(true).download(config);
                                }else {
                                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.system_download_description),INSTANCE.getString(R.string.tishi118)+response.body().getData().getLatestVersion(),new MiddleDialog.onButtonCLickListener2() {
                                        @Override
                                        public void onActivieButtonClick(Object bean, int po) {
                                            if(bean==null){
                                            }else {
                                                UpdaterConfig config = new UpdaterConfig.Builder(INSTANCE)
                                                        .setTitle(getResources().getString(R.string.app_name))
                                                        .setDescription(getString(R.string.system_download_description))
                                                        .setFileUrl(path)
                                                        .setCanMediaScanner(true)
                                                        .build();
                                                Updater.get().showLog(true).download(config);
                                            }
                                        }
                                    }, R.style.registDialog).show();
                                }
                            }else {
                                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi107),R.style.registDialog).show();
                            }
                        }else {
                            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi107),R.style.registDialog).show();
                        }

                    }else {
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<VersionBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof GuanYuActivity){
                    dialog.dismiss();
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

}
