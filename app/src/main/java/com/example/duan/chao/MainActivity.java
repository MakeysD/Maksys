package com.example.duan.chao;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chiclam.android.updater.Updater;
import com.chiclam.android.updater.UpdaterConfig;
import com.example.duan.chao.DCZ_activity.BaseActivity;
import com.example.duan.chao.DCZ_activity.GesturesLockActivity;
import com.example.duan.chao.DCZ_activity.GuanYuActivity;
import com.example.duan.chao.DCZ_activity.LanguageActivity;
import com.example.duan.chao.DCZ_activity.LockActivity;
import com.example.duan.chao.DCZ_activity.LoginEmailActivity;
import com.example.duan.chao.DCZ_activity.ScanActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_activity.TanSuoActivity;
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_authenticator.AccountDb;
import com.example.duan.chao.DCZ_authenticator.AuthenticatorActivity;
import com.example.duan.chao.DCZ_authenticator.OtpProvider;
import com.example.duan.chao.DCZ_authenticator.OtpSource;
import com.example.duan.chao.DCZ_authenticator.OtpSourceException;
import com.example.duan.chao.DCZ_authenticator.TotpClock;
import com.example.duan.chao.DCZ_authenticator.TotpCountdownTask;
import com.example.duan.chao.DCZ_authenticator.TotpCounter;
import com.example.duan.chao.DCZ_authenticator.Utilities;
import com.example.duan.chao.DCZ_authenticator.dataimport.ImportController;
import com.example.duan.chao.DCZ_authenticator.testability.DependencyInjector;
import com.example.duan.chao.DCZ_authenticator.testability.StartActivityListener;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.TimeBean;
import com.example.duan.chao.DCZ_bean.VersionBean;
import com.example.duan.chao.DCZ_jiguang.ExampleUtil;
import com.example.duan.chao.DCZ_jiguang.LocalBroadcastManager;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.DragLayout;
import com.example.duan.chao.DCZ_selft.DragRelativeLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.SwitchButton;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.DisplayUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.MyRoundProcess;
import com.example.duan.chao.DCZ_util.NotificationsUtils;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.DCZ_zhiwen.CryptoObjectHelper;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity{
    private Dialog dialog;
    private MiddleDialog dia;
    //下面的是极光需要
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CONTENT_TYPE = "content_type";
    public static final String KEY_EXTRAS = "extras";
    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    public static boolean isForeground = true;
    //上面的是极光需要
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;
    private MainActivity INSTANCE;
    private DragLayout mDragLayout;
    private DraweeController dra;
    private Boolean boo=true;
    private String version;
    private String path="";

    private static final String LOCAL_TAG = "MainActivity";
    private static final long VIBRATE_DURATION = 200L;
    private Boolean type=true;
    private Boolean Anima_red=true;
    private Boolean Anima_blue=true;
    private static final long TOTP_COUNTDOWN_REFRESH_PERIOD = 100;
    static final int DIALOG_ID_UNINSTALL_OLD_APP = 12;
    static final int DIALOG_ID_SAVE_KEY = 13;
    static final String ACTION_SCAN_BARCODE =MainActivity.class.getName() + ".ScanBarcode";
    private PinInfo[] mUsers = {};//数据
    private TotpCounter mTotpCounter;
    private TotpClock mTotpClock;
    private TotpCountdownTask mTotpCountdownTask;
    private AccountDb mAccountDb;
    private OtpSource mOtpProvider;
    private static final String KEY_OLD_APP_UNINSTALL_INTENT = "oldAppUninstallIntent";
    private Intent mOldAppUninstallIntent;
    private boolean mDataImportInProgress;
    private static final String KEY_SAVE_KEY_DIALOG_PARAMS = "saveKeyDialogParams";
    private SaveKeyDialogParams mSaveKeyDialogParams;
    private boolean mSaveKeyIntentConfirmationInProgress;
    private static final String OTP_SCHEME = "otpauth";
    private static final String TOTP = "totp"; // time-based
    private static final String HOTP = "hotp"; // counter-based
    private static final String SECRET_PARAM = "secret";
    private static final String COUNTER_PARAM = "counter";
    public static final int CHECK_KEY_VALUE_ID = 0;
    public static final int RENAME_ID = 1;
    public static final int REMOVE_ID = 2;
    static final int COPY_TO_CLIPBOARD_ID = 3;
    static final int SCAN_REQUEST = 31337;
    private TextView pinView;
    private double mTotpCountdownPhase;
    private GifDrawable gifFromResource;
    public static Handler mHandler ;
    public static Handler handler ;
    private String sign;
    private Long miss;//请求前的时间
    private Boolean fu=false;   //是否复制内容
    private Boolean amin_shou=true; //收缩动画是否运行结束
    private MyRoundProcess mRoundProcess;
    private static ObjectAnimator anima;
    private static TextView image;
    private static void setLayout(int number,int x, int y){
        Log.i("dczz","t");
        image.setBackgroundResource(R.drawable.hong);
        image.setText(number+"");
        image.setTextSize(18);
        image.setX(x);image.setY(y);
    }
    private void AmimaHandler(){
        PropertyValuesHolder objectAnimatorScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.4f, 1f);
        PropertyValuesHolder objectAnimatorScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.4f, 1f);
        /**同时播放两个动画**/
        anima = ObjectAnimator.ofPropertyValuesHolder(image, objectAnimatorScaleX, objectAnimatorScaleY).setDuration(1000);
        anima.setRepeatCount(ValueAnimator.INFINITE);
        anima.start();
        handler=new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 101:
                        image.setBackground(null);
                        image.setText("");
                        break;
                    default:
                        float b=mRoundProcess.progress/100*30;
                        BigDecimal bd=new BigDecimal(b);
                        //只保留小数点后三位
                        String va=bd.setScale(3,BigDecimal.ROUND_HALF_UP).toString();
                        float value=Float.parseFloat(va);
                        float cha = (value - (30 - (msg.what))) * 1000;
                        anima.setCurrentPlayTime((long) cha);
                        setLayout(msg.what,MyApplication.x,MyApplication.y);break;
                }
            }
        };
    }
    private void initHandler(){
        //下线通知
        mHandler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.tishi115),"",new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int position) {
                                ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                ActivityUtils.getInstance().popAllActivities();
                            }
                        }, R.style.registDialog).show();
                        break;
                    case 2:
                        getVersion();
                        break;
                  /*  case 3:
                        TransitionDrawable transition = (TransitionDrawable)getResources().getDrawable(R.drawable.transition);
                        home.setBackground(transition);
                        transition.startTransition(2000);
                        break;*/
                }
            }
        };
    }
    @BindView(R.id.back)
    View back;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.rl5)
    RelativeLayout rl5;
    @BindView(R.id.rl6)
    RelativeLayout rl6;
    @BindView(R.id.rl7)
    RelativeLayout rl7;
    @BindView(R.id.rl8)
    RelativeLayout rl8;
    @BindView(R.id.rl_have) //授权页
    RelativeLayout rl_have;
    @BindView(R.id.rl_code) //动态码页
    RelativeLayout rl_code;
    @BindView(R.id.home)
    LinearLayout home;
    @BindView(R.id.scan)
    View scan;
    @BindView(R.id.tv_suo)
    TextView tv_suo;
    @BindView(R.id.tv_anquan)
    TextView tv_anquan;
    @BindView(R.id.button2)
    SwitchButton button2;     //指纹锁
    @BindView(R.id.title)
    TextView tv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.zhanghao)
    TextView zhanghao;
    @BindView(R.id.add)     //添加账号
    LinearLayout add;
    @BindView(R.id.iv1)
    SimpleDraweeView iv1;
    @BindView(R.id.iv2)
    SimpleDraweeView iv2;
    @BindView(R.id.language)
    TextView language;
    @BindView(R.id.have)
    TextView have;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.pup)
    RelativeLayout pup;        //弹框
    @BindView(R.id.fuzhi)
    TextView fuzhi;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.gifview)
    GifImageView gif;
    @BindView(R.id.iv)
    SimpleDraweeView iv;
    @BindView(R.id.zhuan)
    SimpleDraweeView zhuan;
    @BindView(R.id.yuan)
    RelativeLayout yuan;
    public static Long number;
    public static TextView ceshi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        Log.i("dcz类名",DisplayUtil.getDisplayWidthPixels(INSTANCE)+"");
        ButterKnife.bind(this);
        JPushInterface.resumePush(getApplicationContext());
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl5).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl6).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl7).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl8).rippleCorner(MyApplication.dp2Px()).create();
        registerMessageReceiver();
        image= (TextView) findViewById(R.id.image);
        iv.setImageURI(null);
        ceshi= (TextView) findViewById(R.id.ceshi);
        home.setBackgroundResource(R.drawable.b_g5);
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");

        AmimaHandler();
        mRoundProcess = (MyRoundProcess) findViewById(R.id.my_round_process);
        // 开启动画
        mRoundProcess.runAnimate(30000);

        setViews();
        duration();//首页动画
        if (savedInstanceState != null) {
            mOldAppUninstallIntent = savedInstanceState.getParcelable(KEY_OLD_APP_UNINSTALL_INTENT);
            mSaveKeyDialogParams = (SaveKeyDialogParams) savedInstanceState.getSerializable(KEY_SAVE_KEY_DIALOG_PARAMS);
        }
        auth();     //谷歌验证码动画
        if (savedInstanceState == null) {
            DependencyInjector.getOptionalFeatures().onAuthenticatorActivityCreated(INSTANCE);
            importDataFromOldAppIfNecessary();
            handleIntent(getIntent());
        }
        getVersion();//版本更新
        setListener();
        initHandler();
        MyApplication.status=false;
        if(MyApplication.uri.equals("http://110.79.11.5/user-safe-api/")){
            tv.setText("测试版—Makeys");
        }else {
            tv.setText(R.string.title14);
        }
    }
    private void duration(){
        zhuan.setImageResource(R.drawable.zhuan);
        iv.setImageResource(R.drawable.progress4);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
        animationDrawable.start();
        // 计算动画执行的时间
        int duration = 0;
        for(int i=0;i<animationDrawable.getNumberOfFrames()-35;i++){
            duration += animationDrawable.getDuration(i);
        }
        // 通过handler发送一个延迟消息
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(INSTANCE, R.anim.rotate2);
                zhuan.setVisibility(View.VISIBLE);
                zhuan.startAnimation(animation);
            }
        }, duration);
    }
    private void auth(){
        mAccountDb = DependencyInjector.getAccountDb();
        mOtpProvider = DependencyInjector.getOtpProvider();
        mTotpCounter = mOtpProvider.getTotpCounter();
        mTotpClock = mOtpProvider.getTotpClock();
        Object savedState = getLastNonConfigurationInstance();
        if (savedState != null) {
            mUsers = (PinInfo[]) savedState;
            for (PinInfo account : mUsers) {
                if (account.isHotp) {
                    account.hotpCodeGenerationAllowed = true;
                }
            }
        }
        pinView = (TextView)findViewById(R.id.pin_value);
        up(mUsers);
    }

    @Override
    public void startActivity(Intent intent) {
        StartActivityListener listener = DependencyInjector.getStartActivityListener();
        if ((listener != null) && (listener.onStartActivityInvoked(this, intent))) {
            return;
        }
        super.startActivity(intent);
    }
    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        if (ACTION_SCAN_BARCODE.equals(action)) {
            scanBarcode();
        } else if (intent.getData() != null) {
            interpretScanResult(intent.getData(), true);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_OLD_APP_UNINSTALL_INTENT, mOldAppUninstallIntent);
        outState.putSerializable(KEY_SAVE_KEY_DIALOG_PARAMS, mSaveKeyDialogParams);
    }
    @Override
    public Object onRetainNonConfigurationInstance() {
        return mUsers;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(getString(R.string.app_name), LOCAL_TAG + ": onNewIntent");
        handleIntent(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateCodesAndStartTotpCountdownTask();
    }
    @Override
    protected void onStop() {
        stopTotpCountdownTask();
        super.onStop();
    }
    private void updateCodesAndStartTotpCountdownTask() {
        stopTotpCountdownTask();
        mTotpCountdownTask = new TotpCountdownTask(mTotpCounter, mTotpClock, TOTP_COUNTDOWN_REFRESH_PERIOD);
        mTotpCountdownTask.setListener(new TotpCountdownTask.Listener() {
            @Override
            public void onTotpCountdown(long millisRemaining) {
                if (isFinishing()) {
                    //没有必要去接触它，因为无论如何，活动正在结束
                    return;
                }
                setTotpCountdownPhaseFromTimeTillNextValue(millisRemaining);
            }
            @Override
            public void onTotpCounterValueChanged() {
                if (isFinishing()) {
                    // 没有必要去接触它，因为无论如何，活动正在结束
                    return;
                }
                refreshVerificationCodes();
            }
        });
        mTotpCountdownTask.startAndNotifyListener();
    }
    private void stopTotpCountdownTask() {
        if (mTotpCountdownTask != null) {
            mTotpCountdownTask.stop();
            mTotpCountdownTask = null;
        }
    }
    /** 显示用户邮件和更新的pin码. */
    protected void refreshUserList() {
        refreshUserList(false);
    }
    private void setTotpCountdownPhase(double phase) {
        mTotpCountdownPhase = phase;
        updateCountdownIndicators();
    }
    private void setTotpCountdownPhaseFromTimeTillNextValue(long millisRemaining) {
        setTotpCountdownPhase(((double) millisRemaining) / Utilities.secondsToMillis(mTotpCounter.getTimeStep()));
    }
    private void refreshVerificationCodes() {
        refreshUserList();
        setTotpCountdownPhase(1.0);
    }
    /**
     * 更新进度条的进度
     * */
    private void updateCountdownIndicators() {
           // Log.i("dcz",mTotpCountdownPhase+"");
            if(mTotpCountdownPhase!=1.0){
                if(type==true){
                    animo(mTotpCountdownPhase);
                    type=false;
                }
                //红色背景
                if(mTotpCountdownPhase<0.3&&mTotpCountdownPhase>0.01){
                    if(Anima_red==true){
                        if(rl_code.getVisibility()==View.VISIBLE){
                            Anima_red=false;
                            Anima_blue=true;
                        }
                    }
                }
                if(mTotpCountdownPhase<0.01){
                    if(Anima_blue==true){
                        if(rl_code.getVisibility()==View.VISIBLE){
                            Anima_blue=false;
                            Anima_red=true;
                        }
                    }
                }
            }
    }
    /**
     * 显示用户邮件和更新的pin码
     * @param isAccountModified if true, force full refresh
     */
    public void refreshUserList(boolean isAccountModified) {
        ArrayList<String> usernames = new ArrayList<String>();
        mAccountDb.getNames(usernames);
        int userCount = usernames.size();
        if (userCount > 0) {
            boolean newListRequired = isAccountModified || mUsers.length != userCount;
            if (newListRequired) {
                mUsers = new PinInfo[userCount];
            }
            for (int i = 0; i < userCount; ++i) {
                String user = usernames.get(i);
                try {
                    computeAndDisplayPin(user, i, false);
                } catch (OtpSourceException ignored) {}
            }
            up(mUsers);
        }
    }
    public void computeAndDisplayPin(String user, int position, boolean computeHotp) throws OtpSourceException {
        PinInfo currentPin;
        if (mUsers[position] != null) {
            currentPin = mUsers[position]; // existing PinInfo, so we'll update it
        } else {
            currentPin = new PinInfo();
            currentPin.pin = getString(R.string.empty_pin);
            currentPin.hotpCodeGenerationAllowed = true;
        }
        AccountDb.OtpType type = mAccountDb.getType(user);
        currentPin.isHotp = (type == AccountDb.OtpType.HOTP);
        currentPin.user = user;
        if (!currentPin.isHotp || computeHotp) {
            currentPin.pin = mOtpProvider.getNextCode(user);
            currentPin.hotpCodeGenerationAllowed = true;
        }
        mUsers[position] = currentPin;
    }
    private void parseSecret(Uri uri, boolean confirmBeforeSave) {
        final String scheme = uri.getScheme().toLowerCase();
        final String path = uri.getPath();
        final String authority = uri.getAuthority();
        final String user;
        final String secret;
        final AccountDb.OtpType type;
        final Integer counter;
        if (!OTP_SCHEME.equals(scheme)) {
            Log.e(getString(R.string.app_name), LOCAL_TAG + ": Invalid or missing scheme in uri");
            showDialog(Utilities.INVALID_QR_CODE);
            return;
        }
        if (TOTP.equals(authority)) {
            type = AccountDb.OtpType.TOTP;
            counter = AccountDb.DEFAULT_HOTP_COUNTER; // only interesting for HOTP
        } else if (HOTP.equals(authority)) {
            type = AccountDb.OtpType.HOTP;
            String counterParameter = uri.getQueryParameter(COUNTER_PARAM);
            if (counterParameter != null) {
                try {
                    counter = Integer.parseInt(counterParameter);
                } catch (NumberFormatException e) {
                    Log.e(getString(R.string.app_name), LOCAL_TAG + ": Invalid counter in uri");
                    showDialog(Utilities.INVALID_QR_CODE);
                    return;
                }
            } else {
                counter = AccountDb.DEFAULT_HOTP_COUNTER;
            }
        } else {
            Log.e(getString(R.string.app_name), LOCAL_TAG + ": Invalid or missing authority in uri");
            showDialog(Utilities.INVALID_QR_CODE);
            return;
        }
        user = validateAndGetUserInPath(path);
        if (user == null) {
            Log.e(getString(R.string.app_name), LOCAL_TAG + ": Missing user id in uri");
            showDialog(Utilities.INVALID_QR_CODE);
            return;
        }
        secret = uri.getQueryParameter(SECRET_PARAM);

        if (secret == null || secret.length() == 0) {
            Log.e(getString(R.string.app_name), LOCAL_TAG +
                    ": Secret key not found in URI");
            showDialog(Utilities.INVALID_SECRET_IN_QR_CODE);
            return;
        }
        if (AccountDb.getSigningOracle(secret) == null) {
            Log.e(getString(R.string.app_name), LOCAL_TAG + ": Invalid secret key");
            showDialog(Utilities.INVALID_SECRET_IN_QR_CODE);
            return;
        }

        if (secret.equals(mAccountDb.getSecret(user)) &&
                counter == mAccountDb.getCounter(user) &&
                type == mAccountDb.getType(user)) {
            return;  // nothing to update.
        }
        if (confirmBeforeSave) {
            mSaveKeyDialogParams = new SaveKeyDialogParams(user, secret, type, counter);
            showDialog(DIALOG_ID_SAVE_KEY);
        } else {
            saveSecretAndRefreshUserList(user, secret, null, type, counter);
        }
    }
    private static String validateAndGetUserInPath(String path) {
        if (path == null || !path.startsWith("/")) {
            return null;
        }
        String user = path.substring(1).trim();
        if (user.length() == 0) {
            return null; // only white spaces.
        }
        return user;
    }
    private void saveSecretAndRefreshUserList(String user, String secret, String originalUser, AccountDb.OtpType type, Integer counter) {
        if (saveSecret(this, user, secret, originalUser, type, counter)) {
            refreshUserList(true);
        }
    }
    public static boolean saveSecret(Context context, String user, String secret, String originalUser, AccountDb.OtpType type, Integer counter) {
        if (originalUser == null) {  // new user account
            originalUser = user;
        }
        if (secret != null) {
            AccountDb accountDb = DependencyInjector.getAccountDb();
            accountDb.update(user, secret, originalUser, type, counter);
            DependencyInjector.getOptionalFeatures().onAuthenticatorActivityAccountSaved(context, user);
            Toast.makeText(context, R.string.secret_saved, Toast.LENGTH_LONG).show();
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(VIBRATE_DURATION);
            return true;
        } else {
            Log.e(LOCAL_TAG, "Trying to save an empty secret key");
            Toast.makeText(context, R.string.error_empty_secret, Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private String idToEmail(long id) {
        return mUsers[(int) id].user;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String user = idToEmail(info.id);
        AccountDb.OtpType type = mAccountDb.getType(user);
        menu.setHeaderTitle(user);
        menu.add(0, COPY_TO_CLIPBOARD_ID, 0, R.string.copy_to_clipboard);
        if (type == AccountDb.OtpType.HOTP) {
            menu.add(0, CHECK_KEY_VALUE_ID, 0, R.string.check_code_menu_item);
        }
        menu.add(0, RENAME_ID, 0, R.string.rename);
        menu.add(0, REMOVE_ID, 0, R.string.context_menu_remove_account);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void scanBarcode() {
        Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
        intentScan.putExtra("SCAN_MODE", "QR_CODE_MODE");
        intentScan.putExtra("SAVE_HISTORY", false);
        try {
            startActivityForResult(intentScan, SCAN_REQUEST);
        } catch (ActivityNotFoundException error) {
            showDialog(Utilities.DOWNLOAD_DIALOG);
        }
    }
    public static Intent getLaunchIntentActionScanBarcode(Context context) {
        return new Intent(MainActivity.ACTION_SCAN_BARCODE)
                .setComponent(new ComponentName(context, AuthenticatorActivity.class));
    }
    private void interpretScanResult(Uri scanResult, boolean confirmBeforeSave) {
        if (DependencyInjector.getOptionalFeatures().interpretScanResult(this, scanResult)) {
            return;
        }
        if (confirmBeforeSave) {
            if (mSaveKeyIntentConfirmationInProgress) {
                Log.w(LOCAL_TAG, "Ignoring save key Intent: previous Intent not yet confirmed by user");
                return;
            }
            mSaveKeyIntentConfirmationInProgress = true;
        }
        if (scanResult == null) {
            showDialog(Utilities.INVALID_QR_CODE);
            return;
        }

        if (OTP_SCHEME.equals(scanResult.getScheme()) && scanResult.getAuthority() != null) {
            parseSecret(scanResult, confirmBeforeSave);
        } else {
            showDialog(Utilities.INVALID_QR_CODE);
        }
    }
    private static class PinInfo {
        private String pin; // 如果不计算计算OTP,或者一个占位符
        private String user;
        private boolean isHotp = false; // 使用按钮是否需要显示出来
        private boolean hotpCodeGenerationAllowed;
    }
    private static final float PIN_TEXT_SCALEX_NORMAL = 1.0f;
    private static final float PIN_TEXT_SCALEX_UNDERSCORE = 0.87f;
    private void importDataFromOldAppIfNecessary() {
        if (mDataImportInProgress) {
            return;
        }
        mDataImportInProgress = true;
        DependencyInjector.getDataImportController().start(this, new ImportController.Listener() {
            @Override
            public void onOldAppUninstallSuggested(Intent uninstallIntent) {
                if (isFinishing()) {
                    return;
                }
                mOldAppUninstallIntent = uninstallIntent;
                showDialog(DIALOG_ID_UNINSTALL_OLD_APP);
            }
            @Override
            public void onDataImported() {
                if (isFinishing()) {
                    return;
                }
                refreshUserList(true);
                DependencyInjector.getOptionalFeatures().onDataImportedFromOldApp(
                        INSTANCE);
            }
            @Override
            public void onFinished() {
                if (isFinishing()) {
                    return;
                }
                mDataImportInProgress = false;
            }
        });
    }
    private static class SaveKeyDialogParams implements Serializable {
        private final String user;
        private final String secret;
        private final AccountDb.OtpType type;
        private final Integer counter;

        private SaveKeyDialogParams(String user, String secret, AccountDb.OtpType type, Integer counter) {
            this.user = user;
            this.secret = secret;
            this.type = type;
            this.counter = counter;
        }
    }
    private void up(PinInfo[]data){
        if(data.length==0){
            return;
        }
        int a = data.length;
        PinInfo currentPin = data[a-1];
        if (getString(R.string.empty_pin).equals(currentPin.pin)) {
            pinView.setTextScaleX(PIN_TEXT_SCALEX_UNDERSCORE);
        } else {
            pinView.setTextScaleX(PIN_TEXT_SCALEX_NORMAL);
        }
        pinView.setText(currentPin.pin);
        animo(mTotpCountdownPhase);
    }
    private void animo(double mTotpCountdownPhase){
        Log.i("dcz进度4",mTotpCountdownPhase+"");
        /*mRoundProcess.restartAnimate(); //重启动画
        anima.start();*/
        mRoundProcess.mAnimator.setCurrentPlayTime(number);
        try {
            if(gifFromResource==null){
                gifFromResource = new GifDrawable(getResources(), R.mipmap.gif4);
                if(OtpProvider.DEFAULT_INTERVAL==30){
                    gifFromResource.setSpeed(1f);
                }else {
                    gifFromResource.setSpeed(0.5f);
                }
                Log.i("dcz进度1",mTotpCountdownPhase+"");
                gifFromResource.seekTo((int) (30000-(30000*mTotpCountdownPhase)));
                gif.setImageDrawable(gifFromResource);
            }else {
                Log.i("DCZ",mTotpCountdownPhase+"");//数字从大变小
                if(mTotpCountdownPhase>0.5){
                    gifFromResource.seekTo(300+(int)(30000-(30000*mTotpCountdownPhase)));
                    Log.i("DCZ2",300+(30000-(30000*mTotpCountdownPhase))+"");//数字从大变小
                }else if(mTotpCountdownPhase<0.01){
                    gifFromResource.seekTo(300);
                    Log.i("DCZ3",300+"");//数字从大变小
                }else {
                    gifFromResource.seekTo((int)(30000-(30000*mTotpCountdownPhase)));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int ima=1;
    private void setViews() {
        // 获取packagemanager的实例  
        TotpCountdownTask.mLastSeenCounterValue=0;
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息  
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packInfo.versionName;
        if(MyApplication.language.equals("ENGLISH")){
            language.setText(R.string.tishi37);
        }else {
            language.setText(R.string.tishi35);
        }
        name.setText(MyApplication.nickname);
        zhanghao.setText(MyApplication.username);
        mDragLayout = (DragLayout) findViewById(R.id.dsl);
        DragRelativeLayout mMainView = (DragRelativeLayout) findViewById(R.id.rl_main);
        mMainView.setDragLayout(mDragLayout);
        if(MyApplication.zhiwen==true){
            button2.setChecked(true);
        }else {
            button2.setChecked(false);
        }
        /*final Animation animation = AnimationUtils.loadAnimation(INSTANCE, R.anim.alpha);
        se.startAnimation(animation);*/
    }

    private void setdialog(){
        Log.i("dcz通知是否打开",NotificationManagerCompat.from(INSTANCE).areNotificationsEnabled()+"");
        if(NotificationManagerCompat.from(INSTANCE).areNotificationsEnabled()==false){
            new MiddleDialog(INSTANCE,this.getString(R.string.tishi114),this.getString(R.string.tishi113),true,new MiddleDialog.onButtonCLickListener2() {
                @Override
                public void onActivieButtonClick(Object bean, int po) {
                    if(bean==null){
                    }else {
                        NotificationsUtils.StartSetting(INSTANCE);
                    }
                }
            }, R.style.registDialog).show();
        }
    }
    private void setListener() {
        have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addAccount();
                home.setBackgroundResource(R.drawable.b_g5);
                code.setVisibility(View.VISIBLE);
                have.setVisibility(View.GONE);
                rl_code.setVisibility(View.GONE);
                rl_have.setVisibility(View.VISIBLE);
            }
        });
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up(mUsers);
                have.setVisibility(View.VISIBLE);
                code.setVisibility(View.GONE);
                if(mTotpCountdownPhase<0.3&&mTotpCountdownPhase>0.01){
                    Anima_blue=true;
                    Anima_red=false;
                }else {
                    Anima_blue=false;
                    Anima_red=true;
                }
                rl_have.setVisibility(View.GONE);
                rl_code.setVisibility(View.VISIBLE);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miss=new Date().getTime();
                time();
            }
        });
        fuzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amin_shou==true){
                    fu=true;
                    tan();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fu=false;
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragLayout.open(true, DragLayout.Direction.Right);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NotificationManagerCompat.from(INSTANCE).areNotificationsEnabled()==false){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi114),INSTANCE.getString(R.string.tishi113),true,new MiddleDialog.onButtonCLickListener2() {
                        @Override
                        public void onActivieButtonClick(Object bean, int po) {
                            if(bean==null){
                            }else {
                                NotificationsUtils.StartSetting(INSTANCE);
                            }
                        }
                    }, R.style.registDialog).show();
                }else {
                    quan();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LoginEmailActivity.class);
                startActivity(intent);
            }
        });
        //安全保护
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, SecurityProtectActivity.class);
                startActivity(intent);
            }
        });
        //手势锁
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果手势密码为0
                if(LockUtil.getPwdStatus(INSTANCE)==true){
                    Intent intent=new Intent(INSTANCE, LockActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(INSTANCE, GesturesLockActivity.class);
                    startActivity(intent);
                }
            }
        });
        //账户安全
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ZhangHuSercurityActivity.class);
                startActivity(intent);
            }
        });
        //指纹锁
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //关于
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, GuanYuActivity.class);
                startActivity(intent);
            }
        });
        //退出当前账户    `
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MiddleDialog(INSTANCE,null,INSTANCE.getString(R.string.tishi100),true,new MiddleDialog.onButtonCLickListener2() {
                    @Override
                    public void onActivieButtonClick(Object bean, int po) {
                        if(bean==null){
                        }else {
                            getData();
                        }
                    }
                }, R.style.registDialog).show();

            }
        });
        //语言
        rl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, LanguageActivity.class);
                startActivity(intent);
            }
        });
        //探索
        rl8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, TanSuoActivity.class);
                startActivity(intent);
            }
        });
        //指纹锁的开关
        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(boo==true){
                    if(isChecked==true){
                        //开启指纹锁
                        boo=false;
                        startZhiwen(true);
                    } else {
                        //关闭指纹锁
                        boo=false;
                        startZhiwen(false);
                    }
                }else {
                    boo=true;
                }
            }
        });
    }
    private void tan(){
        amin_shou=false;
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        a.setDuration(200);
        a.setInterpolator(new LinearInterpolator());
        pup.startAnimation(a);
        pup.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shou();
            }
        },3000);
    }
    private void shou(){
        amin_shou=false;
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        a.setDuration(200);
        a.setInterpolator(new LinearInterpolator());
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                amin_shou=true;
                if(fu==true){
                    // 从API11开始android推荐使用android.content.ClipboardManager
                    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(pinView.getText());
                }
                pup.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pup.startAnimation(a);
    }
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        if(MyApplication.status==true){
            MyApplication.type=1;
            recreate();
        }
        if(LockUtil.getPwdStatus(INSTANCE)==true){
            tv_suo.setText(R.string.main2);
        }else {
            tv_suo.setText(R.string.main3);
        }
        Log.i(getString(R.string.app_name), LOCAL_TAG + ": onResume");
        up(mUsers);
        importDataFromOldAppIfNecessary();
    }
    //手动开启相机权限
    private void quan(){
        if(NotificationsUtils.cameraIsCanUse()==true){
            Log.i("dcz2","有权限");
            Intent intent=new Intent(INSTANCE, ScanActivity.class);
            startActivity(intent);
        }else {
            Log.i("dcz2","没有权限");
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(NotificationsUtils.cameraIsCanUse()==true){
                Log.i("dcz2","有权限");
                Intent intent=new Intent(INSTANCE, ScanActivity.class);
                startActivity(intent);
            }else {
                Log.i("dcz2","没有权限");
            }
        }
    }
    /**
     *   指纹身份验证这里开始
     * */
    private void startZhiwen(Boolean bo){
        //初始化指纹.
        fingerprintManager = FingerprintManagerCompat.from(this);
        //先判断有没有指纹传感器
        if (!fingerprintManager.isHardwareDetected()) {
            // 没有检测到指纹传感器，显示对话框告诉用户
            button2.setChecked(false);
            boo=true;
            new MiddleDialog(INSTANCE,this.getString(R.string.no_sensor_dialog_title),R.style.registDialog).show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 没有一个指纹图像被登记
            button2.setChecked(false);
            boo=true;
            new MiddleDialog(INSTANCE,this.getString(R.string.no_fingerprint_enrolled_dialog_title),R.style.registDialog).show();
        } else {
            //弹框让用户确认指纹
            setDialog(this.getString(R.string.tishi92),bo);
        }
    }
    @Override
    protected void onPause() {
        isForeground = true;
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
        iv.setImageURI(null);
        super.onDestroy();
    }
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.i("dcz","接收到广播");
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    String type = intent.getStringExtra(KEY_CONTENT_TYPE);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    Log.i("dcz",messge);
                    Gson mGson = new Gson();
                    HaveBean result = mGson.fromJson(messge, HaveBean.class);
                    MyApplication.reqFlowId=result.getReqFlowId();
                    MyApplication.reqSysId=result.getReqSysId();
                    Log.i("dcz",result.getReqSysId());
                    Log.i("dcz",type+"type");
                    if(type.equals("2")){//下线通知
                        Intent inten=new Intent(INSTANCE, LoginEmailActivity.class);
                        startActivity(inten);
                        ActivityUtils.getInstance().popAllActivities();
                    }
                }
            } catch (Exception e){
            }
        }
    }
    private void setDialog(String content, final Boolean bo){
        if(dia!=null){
            if(dia.isShowing()){
                dia.dismiss();
            }
        }
        start();
        dia=new MiddleDialog(INSTANCE,content,0,new MiddleDialog.onButtonCLickListener(){
            @Override
            public void onButtonCancel(String string) {
                //是空的时候用户点的取消，否则就是指纹验证成功的回调
                if(string==null){
                    Log.i("dcz","用户点击了取消");
                    if(cancellationSignal!=null){
                        Log.i("dcz","用户取消");
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                    }
                    dia.dismiss();
                    if(MyApplication.zhiwen==true){
                        button2.setChecked(true);
                    }else {
                        button2.setChecked(false);
                    }
                }else {
                    boo=true;
                    if(bo==false){
                        MyApplication.zhiwen=false;MyApplication.sf.edit().putBoolean("zhiwen", false).commit();
                    }else {
                        MyApplication.zhiwen=true;MyApplication.sf.edit().putBoolean("zhiwen", true).commit();
                    }
                    dia.dismiss();
                }
            }
        },R.style.registDialog);
        dia.show();
    }
    private void start(){
        // 指纹身份验证这里开始。
        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (cancellationSignal == null) {
                cancellationSignal = new CancellationSignal();
            }
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    cancellationSignal, myAuthCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(INSTANCE,R.string.setting_error, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(KeyEvent.KEYCODE_BACK==keyCode){
            Intent home=new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return false ;
        }
        return super.onKeyDown(keyCode, event);
    }
    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog.show();
        HttpServiceClient.getInstance().exit_login(MyApplication.device).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    MyApplication.sf.edit().putString("cookie","").commit();
                    MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                    Intent intent=new Intent(INSTANCE, LoginEmailActivity.class);
                    startActivity(intent);
                    ActivityUtils.getInstance().popActivity(INSTANCE);
                    MyApplication.offset= Long.valueOf(0);MyApplication.sf.edit().putLong("offset",0).commit();
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                    Log.d("dcz_数据获取失败",response.message());
                }
            }
            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof MainActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    /***
     *  验证版本
     * */
    public void getVersion(){
        /*if(MyApplication.token.equals("")){
            MyApplication.sf.edit().putString("cookie","").commit();
            Intent intent=new Intent(INSTANCE, LoginEmailActivity.class);
            startActivity(intent);
            ActivityUtils.getInstance().popActivity(INSTANCE);
            MyApplication.offset= Long.valueOf(0);MyApplication.sf.edit().putLong("offset",0).commit();
        }*/
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        HttpServiceClient.getInstance().version(MyApplication.device,"android",version,"1").enqueue(new Callback<VersionBean>() {
            @Override
            public void onResponse(Call<VersionBean> call, Response<VersionBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        Log.i("dcz_1",response.body().getData().getLatestVersion()+"q");
                        Log.i("dcz_2",version+"q");
                        setdialog();//判断是否开启通知
                        if(response.body().getData().getLatestVersion()!=null){
                            Log.i("dcz_比较当前版本与服务器",response.body().getData().getLatestVersion().compareTo(version)+"a");
                            if(response.body().getData().getLatestVersion().compareTo(version)>0){
                                path=response.body().getData().getPath().toString();
                                //强制更新版本
                                if(response.body().getData().getNeededUpdated().equals("1")){
                                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.system_download_description),INSTANCE.getString(R.string.tishi118)+response.body().getData().getLatestVersion(),false,new MiddleDialog.onButtonCLickListener2() {
                                        @Override
                                        public void onActivieButtonClick(Object bean, int po) {
                                            if(bean==null){
                                                ActivityUtils.getInstance().popAllActivities();
                                            }else {
                                                down();
                                            }
                                        }
                                    }, R.style.registDialog).show();
                                }else {
                                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.system_download_description),INSTANCE.getString(R.string.tishi118)+response.body().getData().getLatestVersion(),true,new MiddleDialog.onButtonCLickListener2() {
                                        @Override
                                        public void onActivieButtonClick(Object bean, int po) {
                                            if(bean==null){
                                            }else {
                                                down();
                                            }
                                        }
                                    }, R.style.registDialog).show();
                                }
                            }
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
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof MainActivity){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    /**
     *  时间同步
     * */
    private void time(){
        if(ShebeiUtil.wang(this).equals("0")){
            ContentUtil.makeToast(INSTANCE,INSTANCE.getString(R.string.tishi116));
            return;
        }
        String max= RandomUtil.RandomNumber();
        String str ="millisecond="+miss+"&nonce="+max;
        byte[] data = str.getBytes();
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpServiceClient.getInstance().time(miss,max,sign).enqueue(new Callback<TimeBean>() {
            @Override
            public void onResponse(Call<TimeBean> call, Response<TimeBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        type=true;
                        MyApplication.DEFAULT_INTERVAL=response.body().getData().getDefaultIntervalInSecond();MyApplication.sf.edit().putInt("DEFAULT_INTERVAL",response.body().getData().getDefaultIntervalInSecond()).commit();
                        MyApplication.PIN_LENGTH=response.body().getData().getTotpCodeLength();MyApplication.sf.edit().putInt("PIN_LENGTH",response.body().getData().getTotpCodeLength()).commit();
                        TotpCountdownTask.mLastSeenCounterValue=0;
                        Long millis = response.body().getData().getMillisecond();
                        long a = millis + (new Date().getTime() - miss) / 2;
                        MyApplication.offset=new Date().getTime()-a;MyApplication.sf.edit().putLong("offset",new Date().getTime()-a).commit();
                        Log.i("dcz差额",MyApplication.offset+"");
                        ContentUtil.makeToast(INSTANCE,INSTANCE.getString(R.string.tishi137));
                        auth();
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
            public void onFailure(Call<TimeBean> call, Throwable t) {
                dialog.dismiss();
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof MainActivity){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
    private void down(){
        UpdaterConfig config = new UpdaterConfig.Builder(INSTANCE)
                .setTitle(getResources().getString(R.string.app_name))
                .setDescription(getString(R.string.system_download_description))
                .setFileUrl(path)
                .setCanMediaScanner(true)
                .build();
        Updater.get().showLog(true).download(config);
        Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        INSTANCE.startActivity(viewDownloadIntent);
        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
    }
}
