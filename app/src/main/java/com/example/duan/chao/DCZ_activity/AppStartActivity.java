package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.UserStateBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  APP启动页
 *
 * */
public class AppStartActivity extends BaseActivity {
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private Handler mHandler;
    private ImageView iv;
    private String content;
    private String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用LayoutInflater来加载activity_splash.xml视图
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_app_start, null);
        /**
         * 这里不能使用findViewById(R.layout.acitivyt_spash)方法来加载
         * 因为还没有开始调用setContentView()方法，也就是说还没给当前的Activity
         * 设置视图，当前Activity Root View为null，findViewById()方法是从当前
         * Activity的Root View中获取子视图，所以这时候会报NullPointerException异常
         *
         * View rootView = findViewById(R.layout.activity_splash);
         *
         */
        setContentView(rootView);
        MyApplication.App_key=getIntent().getStringExtra("App_key");
        if(MyApplication.App_key!=null){
            MyApplication.Ssokey="1";
        }

        MyApplication.App_name=getIntent().getStringExtra("App_name");
        MyApplication.packname=getIntent().getStringExtra("packname");
        MyApplication.pathName=getIntent().getStringExtra("pathname");
        MyApplication.redirect_uri = getIntent().getStringExtra("redirect_uri");
        MyApplication.scope = getIntent().getStringExtra("scope");
        MyApplication.state=getIntent().getStringExtra("state");
        Log.i("App_key",MyApplication.App_key+"zzz");
   //     if (!TextUtils.isEmpty(MyApplication.redirect_uri) ){
//        if(this.getReferrer() != null) {
//            MyApplication.webPackName = this.getReferrer().getAuthority();
//            Log.e("包名", "getAuthority=" + this.getReferrer().getAuthority() + "    getEncodedPath=" +
//                    this.getReferrer().getEncodedPath() + "    getHost=" + this.getReferrer().getHost() + "   getPath= "
//                    + this.getReferrer().getPath() + "     getScheme=" + this.getReferrer().getScheme() + "   getEncodedAuthority="
//            +this.getReferrer().getEncodedAuthority()+"   getEncodedUserInfo="+this.getReferrer().getEncodedUserInfo()+"   getFragment="+
//            this.getReferrer().getFragment()+"    getEncodedSchemeSpecificPart="+this.getReferrer().getEncodedSchemeSpecificPart()+"     getUserInfo="
//            +this.getReferrer().getUserInfo()+"     getQuery="+this.getReferrer().getQuery());//mReferrer
//        }
   //     }
        Log.e("kk","App_key:"+MyApplication.App_key+"+redirect_uri:"+MyApplication.redirect_uri+"+scope:"+MyApplication.scope+"+state:"+MyApplication.state);
        quan();
       /* mHandler = new Handler();
        //初始化渐变动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        //开始播放动画
        rootView.startAnimation(animation);
        //设置动画监听器
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当监听到动画结束时，开始跳转到MainActivity中去
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        suo();
                    }
                });
            }
        });*/
    }

    private void suo() {
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            Log.i("dcz_flag",i_getvalue.getFlags()+"");
            Uri uri = i_getvalue.getData();
            Log.i("dcz_uri",uri+"");
            MyApplication.webWay = uri.toString();
            if (MyApplication.webWay.startsWith("makeys://AppStartActivity/identityauthorize")){
                MyApplication.webUserName = uri.getQueryParameter("username");
                MyApplication.redirect_uri = uri.getQueryParameter("redirectURI");
            }else {
                MyApplication.App_key = uri.getQueryParameter("appKey");
                MyApplication.App_name = uri.getQueryParameter("displayName");
                MyApplication.redirect_uri = uri.getQueryParameter("redirectURI");
            }
            if(i_getvalue.getFlags()!=269484032){
                MyApplication.Webkey="1";
            }
        }
        //判断是否登录
        if(MyApplication.token.equals("")){
            Intent intent = new Intent(AppStartActivity.this, LoginEmailActivity.class);
            startActivity(intent);
            //判断是否是第一次登录
         /*   if(MyApplication.first){
                Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
                startActivity(intent);
            }*/
        }else {
            //判断是否设置过指纹锁
        /*    if(MyApplication.zhiwen==true){
                Intent intent = new Intent(AppStartActivity.this, ZhiwenActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                //判断当前是否设置过手势锁密码
            }else if(LockUtil.getPwdStatus(this)==true&& MyApplication.suo==true){
                Intent intent=new Intent(this,StartLockActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }else {*/
            if(MyApplication.Webkey!=null){
                if (MyApplication.webWay.startsWith("makeys://AppStartActivity/identityauthorize")) {
                    HttpServiceClient.getInstance().userState(null).enqueue(new Callback<UserStateBean>() {
                        @Override
                        public void onResponse(Call<UserStateBean> call, Response<UserStateBean> response) {
                            if(response.isSuccessful()){
                                if(response.body()!=null){
                                    if(response.body().getCode().equals("10516")){
                                        MyApplication.sf.edit().putString("cookie","").commit();
                                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                                        MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                                        new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),getString(R.string.tishi101),getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                                            @Override
                                            public void onActivieButtonClick(Object bean, int position) {
                                                ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                                ActivityUtils.getInstance().popAllActivities();
                                            }
                                        }, R.style.registDialog).show();
                                        return;
                                    }
                                    if(response.body().getCode().equals("20000")){
                                        Log.i("dcz_code",response.body().getData().getCode());
                                        if(response.body().getData().getStep()==0){
                                            Intent intent=new Intent(AppStartActivity.this,PersonData2Activity.class);
                                            intent.putExtra("state",response.body().getData().getCode());
                                            intent.putExtra("content",response.body().getData().getDescription()+"");
                                            startActivity(intent);
                                        }else {
                                            Intent intent=new Intent(AppStartActivity.this,PersonDataActivity.class);
                                            intent.putExtra("state",response.body().getData().getCode());
                                            intent.putExtra("content",response.body().getData().getDescription()+"");
                                            startActivity(intent);
                                        }
                                    }else {
                                        if(!response.body().getCode().equals("20003")){
                                            new MiddleDialog(AppStartActivity.this,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                                        }
                                    }
                                }else {
                                    Log.d("dcz","返回的数据是空的");
                                }
                            }else {
                                Log.d("dcz","获取数据失败");
                            }
                        }
                        @Override
                        public void onFailure(Call<UserStateBean> call, Throwable t) {
                            if(ActivityUtils.getInstance().getCurrentActivity() instanceof ZhangHuSercurityActivity){
                                new MiddleDialog(AppStartActivity.this,getString(R.string.tishi72),R.style.registDialog).show();
                            }
                        }
                    });
                }else {
                    Intent intent = new Intent(this, WebAuthorActivity.class);
                    startActivity(intent);
                }
                MyApplication.Webkey=null;
            }else if(MyApplication.Ssokey!=null){
                Intent intent = new Intent(this,AuthorActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                startActivity(intent);
            }
         //   }
        }
        ActivityUtils.getInstance().popActivity(this);
    }

    private void quan(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            Log.i("dcz","执行3");
        } else {
            Log.i("dcz","执行2");
            //延迟2S跳转
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    suo();
                }
            }, 700);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i("dcz","执行");
                }else {
                    ActivityUtils.getInstance().popActivity(this);
                }
                break;
            default:
                break;
        }
    }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Log.i("dcz","按下了返回键");
            ActivityUtils.getInstance().popActivity(this);
        }
}
