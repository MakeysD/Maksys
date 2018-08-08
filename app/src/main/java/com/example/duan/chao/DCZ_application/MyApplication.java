package com.example.duan.chao.DCZ_application;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.example.duan.chao.DCZ_authenticator.AccountDb;
import com.example.duan.chao.DCZ_authenticator.FileUtilities;
import com.example.duan.chao.DCZ_authenticator.testability.DependencyInjector;
import com.example.duan.chao.DCZ_jiguang.Logger;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by duan on 2017/5/22.
 */

public class MyApplication extends Application{
    //网路连接  
    private boolean isWifi;//wifi是否连接  
    private boolean isMobile;//手机是否连接  
    private boolean isNetworkConn;//是否有网  
    public static Boolean AppOnForeground=false;//是否是从后台进入的前台
    public static Long start_time;//退到后台的当前时间
    public static int type=0;//判断刷新界面是否放入栈中
    public static HashMap map=new HashMap();
    public static String classname;
    public static String code="CN";
    public static  Dialog dialog;
    public static int x;
    public static int y;
    public static String pri_key;//私钥
    public static String pub_key;//公钥
    public static String uri="";
    public static String ur="";
    public static String pkge="com.example.duan.chao";
    private static final String TAG = "JIGUANG-Example";
    public static boolean zhiwen=false;
    public static int zhiwen_namber=0;
    private static Context context;
    public static boolean first=true;//是否为第一次登录
    public static boolean isLogin=false;
    public static Boolean suo=true;     //是否打开手势解锁页面
    public static String qiniu="https://pic.bincrea.com/";
    public static String city="";
    public static String language="";
    public static String xitong="";
    public static Boolean status=false;

    public static String App_key;
    public static String Webkey;
    public static String Ssokey;
    public static String App_name;
    public static String redirect_uri;
    public static String scope;
    public static String state;
    public static String packname;
    public static String pathName;

    public static String device;
    public static String username;
    public static String nickname;
    public static String token;
    public static String mobile;
    public static String xinghao;
    public static String currentapiVersion="Android "+ShebeiUtil.getSystemVersion();
    public static String rid="";
    public static String password;
    public static String brand;
    public static String reqSysId;
    public static String reqFlowId;
    public static String sms_type="1";//是需要验证短信，0是不需要验证短信
    public static ClearableCookieJar jar;
    public static String cookie;
    //偏好设置
    public static SharedPreferences sf;
    public static Long offset;
    public static Integer PIN_LENGTH;
    public static Integer DEFAULT_INTERVAL;
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "[MyApplication] onCreate");
        context=getApplicationContext();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        Fresco.initialize(this);
        bugly();
       // CodeUtil.pushcode(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "87666fdd22", false);
        try {//authenticator
            FileUtilities.restrictAccessToOwnerOnly(
                    getApplicationContext().getApplicationInfo().dataDir);
        } catch (Throwable e) {
        }
        DependencyInjector.configureForProductionIfNotConfigured(getApplicationContext());
        sf= PreferenceManager.getDefaultSharedPreferences(this);
        first = sf.getBoolean("first",true);
        zhiwen=sf.getBoolean("zhiwen",false);
        token=sf.getString("token","");
        username=sf.getString("username","");
        city=sf.getString("city","");
        nickname=sf.getString("nickname","");
        mobile=sf.getString("mobile","");
        password=sf.getString("password","");
        sms_type=sf.getString("sms_type","1");
        pri_key=sf.getString("pri_key","");
        pub_key=sf.getString("pub_key","");
        language=sf.getString("language","");
        String yu = getBaseContext().getResources().getConfiguration().locale.toString();
        Log.i("dcz_系统语言",yu);
       /* if(yu.equals("en_US")||yu.equals("en_GB")){
            language=sf.getString("language","ENGLISH");
        }else if(yu.equals("th_TH")){
            language=sf.getString("language","TAI");
        }else {
            language=sf.getString("language","CHINESE");
        }*/
        if(yu.equals("en_US")){     //对于多机型，这个为了保存系统语言
            xitong=sf.getString("xitong","en_US");
        }else if(yu.equals("en_GB")){
            xitong=sf.getString("xitong","en_GB");
        }else if(yu.equals("th_TH")){
            xitong=sf.getString("xitong","th_TH");
        }else if(yu.equals("zh_CN")){
            xitong=sf.getString("xitong","zh_CN");
        }else if(yu.equals("sk_SK")){
            xitong=sf.getString("xitong","sk_SK");
        }else if(yu.equals("vi_VN")){
            xitong=sf.getString("xitong","vi_VN");
        }else if(yu.equals("zh_TW")){
            xitong=sf.getString("xitong","zh_TW");
        }else {
            xitong=sf.getString("xitong","zh_CN");
        }
        offset=sf.getLong("offset",0);
        cookie=sf.getString("cookie","");
        uri=sf.getString("uri","http://110.79.11.5/user-safe-api/");
        ur=sf.getString("ur","http://test-makeys.qeveworld.com/#/register");
        /*uri=sf.getString("uri","http://api.qeveworld.com/user-safe-api/");
        ur=sf.getString("ur","http://makeys.qeveworld.com/#/register");*/
        PIN_LENGTH=sf.getInt("PIN_LENGTH",8);
        DEFAULT_INTERVAL=sf.getInt("DEFAULT_INTERVAL",30);
        Log.i("dcz_first",first+"");

        Log.i("dcz_设备ID", ShebeiUtil.getDeviceId(this));
        Log.i("dcz_设备ID2",ShebeiUtil.getUniquePsuedoID());
        Log.i("dcz_设备md5", DSA.md5(ShebeiUtil.getDeviceId(this)));
        Log.i("dcz_设备md52",DSA.md5(ShebeiUtil.getUniquePsuedoID()));
        Log.i("dcz_设备型号",ShebeiUtil.getPhoneModel());
        Log.i("dcz_手机品牌",ShebeiUtil.getPhoneBrand());
        Log.i("dcz_手机版本",ShebeiUtil.getSystemVersion());
        device=DSA.md5(ShebeiUtil.getDeviceId(this));
        xinghao=ShebeiUtil.getPhoneModel();
    }
    private void bugly(){
        setStrictMode();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        /**
         *  全量升级状态回调
         */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
            }
            @Override
            public void onUpgradeSuccess(boolean b) {
            }
            @Override
            public void onUpgradeNoVersion(boolean b) {
                //  Toast.makeText(getApplicationContext(), "最新版本", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onUpgrading(boolean b) {
                // Toast.makeText(getApplicationContext(), "onUpgrading", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDownloadCompleted(boolean b) {
            }
        };

        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                //Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                // Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "%s %d%%", Beta.strNotificationDownloading, (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDownloadSuccess(String patchFilePath) {
                //Toast.makeText(getApplicationContext(), patchFilePath, Toast.LENGTH_SHORT).show();
//                Beta.applyDownloadedPatch();
            }
            @Override
            public void onDownloadFailure(String msg) {
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onApplySuccess(String msg) {
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onApplyFailure(String msg) {
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPatchRollback() {
                //Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
            }
        };
        long start = System.currentTimeMillis();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, "87666fdd22", true);
        long end = System.currentTimeMillis();
        Log.e("init time--->", end - start + "ms");
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    public static Context getContext(){
        return context;
    }
   public static float dp2Px() {
       return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
   }
    private static final long VIBRATE_DURATION = 200L;
    public static boolean saveSecret(Context context, String user, String secret, String originalUser, AccountDb.OtpType type, Integer counter) {
        if (originalUser == null) {  // new user account
            originalUser = user;
        }
        if (secret != null) {
            AccountDb accountDb = DependencyInjector.getAccountDb();
            accountDb.update(user, secret, originalUser, type, counter);
            DependencyInjector.getOptionalFeatures().onAuthenticatorActivityAccountSaved(context, user);
           // Toast.makeText(context, R.string.secret_saved, Toast.LENGTH_LONG).show();
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(VIBRATE_DURATION);
            return true;
        } else {
            Toast.makeText(context,"error", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
