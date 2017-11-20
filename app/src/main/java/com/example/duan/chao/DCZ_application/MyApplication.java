package com.example.duan.chao.DCZ_application;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.example.duan.chao.DCZ_authenticator.AccountDb;
import com.example.duan.chao.DCZ_authenticator.FileUtilities;
import com.example.duan.chao.DCZ_authenticator.testability.DependencyInjector;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_jiguang.Logger;
import com.example.duan.chao.DCZ_util.CodeUtil;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static String code="86";
    public static  Dialog dialog;
    //public static String private_key="MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUeH+RT36cvXgOYDinmQUodHggFM4=";
   // public static String public_key="MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBAMgkWZhJlOwjqIZiHOxVrKpyruWrgvk9xITInmwRT+wqLS64flu9E8/FgPHDUlEN6ET/JA8xGuHPMQFUqbUFXOhZzcTXFS4UAEOOrYCJ7HWroC/VX310zq3dphCN0mUsolOuYYB+/W/Qm18alqekL6n0p59VHi3UNbRiZyKkUSbE";
    public static String pri_key;//私钥
    public static String pub_key;//公钥
    //http://api.qeveworld.com/user-safe-api/
    public static String uri="http://110.79.11.5/user-safe-api/";
    //http://makeys.qeveworld.com/#/register
    public static String ur="http://test-makeys.qeveworld.com/#/register";
    private static final String TAG = "JIGUANG-Example";
    public static boolean zhiwen=false;
    public static int zhiwen_namber=0;
    private static Context context;
    public static boolean first=true;//是否为第一次登录
    public static boolean isLogin=false;
    public static Boolean suo=true;     //是否打开手势解锁页面
    public static String qiniu="https://pic.bincrea.com/";
    public static String city="中国";
    public static List<CityBean> city_list=new ArrayList<>();
    public static String language="CHINESE";
    public static Boolean status=false;

    public static String device;
    public static String username;
    public static String nickname;
    public static String token;
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
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "[MyApplication] onCreate");
        context=getApplicationContext();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        Fresco.initialize(this);
        CodeUtil.pushcode(getApplicationContext());
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
        password=sf.getString("password","");
        sms_type=sf.getString("sms_type","1");
        pri_key=sf.getString("pri_key","");
        pub_key=sf.getString("pub_key","");
        language=sf.getString("language","");
        offset=sf.getLong("offset",0);
        cookie=sf.getString("cookie","");
        uri=sf.getString("uri","http://110.79.11.5/user-safe-api/");
        ur=sf.getString("ur","http://test-makeys.qeveworld.com/#/register");
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
            Toast.makeText(context, R.string.error_empty_secret, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
