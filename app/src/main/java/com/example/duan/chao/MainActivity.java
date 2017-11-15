package com.example.duan.chao;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.ClipboardManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.example.duan.chao.DCZ_activity.ZhangHuSercurityActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_authenticator.AccountDb;
import com.example.duan.chao.DCZ_authenticator.AuthenticatorActivity;
import com.example.duan.chao.DCZ_authenticator.CheckCodeActivity;
import com.example.duan.chao.DCZ_authenticator.CountdownIndicator;
import com.example.duan.chao.DCZ_authenticator.OtpSource;
import com.example.duan.chao.DCZ_authenticator.OtpSourceException;
import com.example.duan.chao.DCZ_authenticator.SettingsActivity;
import com.example.duan.chao.DCZ_authenticator.TotpClock;
import com.example.duan.chao.DCZ_authenticator.TotpCountdownTask;
import com.example.duan.chao.DCZ_authenticator.TotpCounter;
import com.example.duan.chao.DCZ_authenticator.Utilities;
import com.example.duan.chao.DCZ_authenticator.dataimport.ImportController;
import com.example.duan.chao.DCZ_authenticator.testability.DependencyInjector;
import com.example.duan.chao.DCZ_authenticator.testability.StartActivityListener;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
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
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.NotificationsUtils;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.DCZ_zhiwen.CryptoObjectHelper;
import com.example.duan.chao.DCZ_zhiwen.MyAuthCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity{
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private Dialog dialog;
    private LoginOkBean data;
    private MediaPlayer player;
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

    private Handler handler = null;
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
    private static final long TOTP_COUNTDOWN_REFRESH_PERIOD = 100;
    private static final long HOTP_MIN_TIME_INTERVAL_BETWEEN_CODES = 5000;
    private static final long HOTP_DISPLAY_TIMEOUT = 2 * 60 * 1000;
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
    private GifImageView gif;
    private TextView pinView;
    private double mTotpCountdownPhase;
    private GifDrawable gifFromResource;

    public static Handler mHandler ;
    private void initHandler(){
        //下线通知
        mHandler = new Handler(){
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
    @BindView(R.id.scan)
    ImageView scan;

    @BindView(R.id.tv_suo)
    TextView tv_suo;
    @BindView(R.id.tv_anquan)
    TextView tv_anquan;
    @BindView(R.id.button2)
    SwitchButton button2;     //指纹锁
    @BindView(R.id.tv)
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

    @BindView(R.id.shuaxin)
    ImageView shuaxin;
    @BindView(R.id.bangzhu)
    ImageView bangzhu;

    @BindView(R.id.language)
    TextView language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE=this;
        Log.i("dcz类名",INSTANCE.getLocalClassName());
        ButterKnife.bind(this);
        JPushInterface.resumePush(getApplicationContext());
        CanRippleLayout.Builder.on(rl1).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl2).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl3).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl5).rippleCorner(MyApplication.dp2Px()).create();
        CanRippleLayout.Builder.on(rl6).rippleCorner(MyApplication.dp2Px()).create();
        registerMessageReceiver();
        setViews();
        if (savedInstanceState != null) {
            mOldAppUninstallIntent = savedInstanceState.getParcelable(KEY_OLD_APP_UNINSTALL_INTENT);
            mSaveKeyDialogParams = (SaveKeyDialogParams) savedInstanceState.getSerializable(KEY_SAVE_KEY_DIALOG_PARAMS);
        }
        auth();
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
            tv.setVisibility(View.VISIBLE);
        }else {
            tv.setVisibility(View.GONE);
        }
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
        gif=(GifImageView)findViewById(R.id.gifview);
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
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        StartActivityListener listener = DependencyInjector.getStartActivityListener();
        if ((listener != null) && (listener.onStartActivityInvoked(this, intent))) {
            return;
        }
        super.startActivityForResult(intent, requestCode);
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
        CountdownIndicator indicator = (CountdownIndicator)findViewById(R.id.countdown_icon);
        if (indicator != null) {
            indicator.setPhase(mTotpCountdownPhase);
            if(mTotpCountdownPhase!=1.0){
                if(type==true){
                    animo(mTotpCountdownPhase);
                    type=false;
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
    public void computeAndDisplayPin(String user, int position,
                                     boolean computeHotp) throws OtpSourceException {
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
        // path is "/user", so remove leading "/", and trailing white spaces
        String user = path.substring(1).trim();
        if (user.length() == 0) {
            return null; // only white spaces.
        }
        return user;
    }
    private void saveSecretAndRefreshUserList(String user, String secret,
                                              String originalUser, AccountDb.OtpType type, Integer counter) {
        if (saveSecret(this, user, secret, originalUser, type, counter)) {
            refreshUserList(true);
        }
    }

    public static boolean saveSecret(Context context, String user, String secret,
                                     String originalUser, AccountDb.OtpType type, Integer counter) {
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
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        final String user = idToEmail(info.id); // final so listener can see value
        switch (item.getItemId()) {
            case COPY_TO_CLIPBOARD_ID:
                ClipboardManager clipboard =
                        (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(mUsers[(int) info.id].pin);
                return true;
            case CHECK_KEY_VALUE_ID:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(this, CheckCodeActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;
            case RENAME_ID:
                final Context context = this; // final so listener can see value
                final View frame = getLayoutInflater().inflate(R.layout.rename,
                        (ViewGroup) findViewById(R.id.rename_root));
                final EditText nameEdit = (EditText) frame.findViewById(R.id.rename_edittext);
                nameEdit.setText(user);
                new AlertDialog.Builder(this)
                        .setTitle(String.format(getString(R.string.rename_message), user))
                        .setView(frame)
                        .setPositiveButton(R.string.submit,
                                this.getRenameClickListener(context, user, nameEdit))
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            case REMOVE_ID:
                View promptContentView =
                        getLayoutInflater().inflate(R.layout.remove_account_prompt, null, false);
                WebView webView = (WebView) promptContentView.findViewById(R.id.web_view);
                webView.setBackgroundColor(Color.TRANSPARENT);
                double pixelsPerDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()) / 10d;
                //webView.getSettings().setDefaultFontSize((int) (mEnterPinPrompt.getTextSize() / pixelsPerDip));
                Utilities.setWebViewHtml(
                        webView,
                        "<html><body style=\"background-color: transparent;\" text=\"white\">"
                                + getString(
                                mAccountDb.isGoogleAccount(user)
                                        ? R.string.remove_google_account_dialog_message
                                        : R.string.remove_account_dialog_message)
                                + "</body></html>");

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.remove_account_dialog_title, user))
                        .setView(promptContentView)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.remove_account_dialog_button_remove,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mAccountDb.delete(user);
                                        refreshUserList(true);
                                    }
                                }
                        )
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private DialogInterface.OnClickListener getRenameClickListener(final Context context,
                                                                   final String user, final EditText nameEdit) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String newName = nameEdit.getText().toString();
                if (newName != user) {
                    if (mAccountDb.nameExists(newName)) {
                        Toast.makeText(context, R.string.error_exists, Toast.LENGTH_LONG).show();
                    } else {
                        saveSecretAndRefreshUserList(newName,
                                mAccountDb.getSecret(user), user, mAccountDb.getType(user),
                                mAccountDb.getCounter(user));
                    }
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_account:
                addAccount();
                return true;
            case R.id.settings:
                showSettings();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(getString(R.string.app_name), LOCAL_TAG + ": onActivityResult");
        if (requestCode == SCAN_REQUEST && resultCode == Activity.RESULT_OK) {
            String scanResult = (intent != null) ? intent.getStringExtra("SCAN_RESULT") : null;
            Uri uri = (scanResult != null) ? Uri.parse(scanResult) : null;
            interpretScanResult(uri, false);
        }
    }

    private void addAccount() {
        DependencyInjector.getOptionalFeatures().onAuthenticatorActivityAddAccount(this);
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

    private void showSettings() {
        Intent intent = new Intent();
        intent.setClass(this, SettingsActivity.class);
        startActivity(intent);
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

    @Override
    protected Dialog onCreateDialog(final int id) {
        Dialog dialog = null;
        switch(id) {
            case Utilities.DOWNLOAD_DIALOG:
                AlertDialog.Builder dlBuilder = new AlertDialog.Builder(this);
                dlBuilder.setTitle(R.string.install_dialog_title);
                dlBuilder.setMessage(R.string.install_dialog_message);
                dlBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                dlBuilder.setPositiveButton(R.string.install_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(Utilities.ZXING_MARKET));
                                try {
                                    startActivity(intent);
                                }
                                catch (ActivityNotFoundException e) { // if no Market app
                                    intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(Utilities.ZXING_DIRECT));
                                    startActivity(intent);
                                }
                            }
                        }
                );
                dlBuilder.setNegativeButton(R.string.cancel, null);
                dialog = dlBuilder.create();
                break;

            case DIALOG_ID_SAVE_KEY:
                final SaveKeyDialogParams saveKeyDialogParams = mSaveKeyDialogParams;
                dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.save_key_message)
                        .setMessage(saveKeyDialogParams.user)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        saveSecretAndRefreshUserList(
                                                saveKeyDialogParams.user,
                                                saveKeyDialogParams.secret,
                                                null,
                                                saveKeyDialogParams.type,
                                                saveKeyDialogParams.counter);
                                    }
                                })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        removeDialog(id);
                        onSaveKeyIntentConfirmationPromptDismissed();
                    }
                });
                break;

            case Utilities.INVALID_QR_CODE:
                dialog = createOkAlertDialog(R.string.error_title, R.string.error_qr,
                        android.R.drawable.ic_dialog_alert);
                markDialogAsResultOfSaveKeyIntent(dialog);
                break;

            case Utilities.INVALID_SECRET_IN_QR_CODE:
                dialog = createOkAlertDialog(
                        R.string.error_title, R.string.error_uri, android.R.drawable.ic_dialog_alert);
                markDialogAsResultOfSaveKeyIntent(dialog);
                break;

            case DIALOG_ID_UNINSTALL_OLD_APP:
                dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.dataimport_import_succeeded_uninstall_dialog_title)
                        .setMessage(
                                DependencyInjector.getOptionalFeatures().appendDataImportLearnMoreLink(
                                        this,
                                        getString(R.string.dataimport_import_succeeded_uninstall_dialog_prompt)))
                        .setCancelable(true)
                        .setPositiveButton(
                                R.string.button_uninstall_old_app,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        startActivity(mOldAppUninstallIntent);
                                    }
                                })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                break;

            default:
                dialog =
                        DependencyInjector.getOptionalFeatures().onAuthenticatorActivityCreateDialog(this, id);
                if (dialog == null) {
                    dialog = super.onCreateDialog(id);
                }
                break;
        }
        return dialog;
    }

    private void markDialogAsResultOfSaveKeyIntent(Dialog dialog) {
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onSaveKeyIntentConfirmationPromptDismissed();
            }
        });
    }
    private void onSaveKeyIntentConfirmationPromptDismissed() {
        mSaveKeyIntentConfirmationInProgress = false;
    }

    private Dialog createOkAlertDialog(int titleId, int messageId, int iconId) {
        return new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setMessage(messageId)
                .setIcon(iconId)
                .setPositiveButton(R.string.ok, null)
                .create();
    }

    private static class PinInfo {
        private String pin; // 如果不计算计算OTP,或者一个占位符
        private String user;
        private boolean isHotp = false; // 使用按钮是否需要显示出来
        private boolean hotpCodeGenerationAllowed;
    }

    private static final float PIN_TEXT_SCALEX_NORMAL = 1.0f;
    private static final float PIN_TEXT_SCALEX_UNDERSCORE = 0.87f;

    private class NextOtpButtonListener implements View.OnClickListener {
        private final Handler mHandler = new Handler();
        private final PinInfo mAccount;

        private NextOtpButtonListener(PinInfo account) {
            mAccount = account;
        }

        @Override
        public void onClick(View v) {
            int position = findAccountPositionInList();
            if (position == -1) {
                throw new RuntimeException("Account not in list: " + mAccount);
            }
            try {
                computeAndDisplayPin(mAccount.user, position, true);
            } catch (OtpSourceException e) {
                DependencyInjector.getOptionalFeatures().onAuthenticatorActivityGetNextOtpFailed(INSTANCE, mAccount.user, e);
                return;
            }
            final String pin = mAccount.pin;
            mAccount.hotpCodeGenerationAllowed = false;

        }
        private int findAccountPositionInList() {
            for (int i = 0, len = mUsers.length; i < len; i++) {
                if (mUsers[i] == mAccount) {
                    return i;
                }
            }

            return -1;
        }
    }
    private void up(PinInfo[]data){
        if(data.length==0){
            return;
        }
        PinInfo currentPin = data[0];
        if (getString(R.string.empty_pin).equals(currentPin.pin)) {
            pinView.setTextScaleX(PIN_TEXT_SCALEX_UNDERSCORE);
        } else {
            pinView.setTextScaleX(PIN_TEXT_SCALEX_NORMAL);
        }
        pinView.setText(currentPin.pin);
        animo(mTotpCountdownPhase);
    }
    private void animo(double mTotpCountdownPhase){
        try {
            if(gifFromResource==null){
                gifFromResource = new GifDrawable(getResources(), R.mipmap.gif4);
                //gifFromResource.setSpeed(1.135f);
                Log.i("dcz进度",(int) (10-mTotpCountdownPhase*10)+"");
                gifFromResource.seekTo((int) (30000-(30000*mTotpCountdownPhase)));
                gif.setImageDrawable(gifFromResource);
            }else {
                Log.i("DCZ",mTotpCountdownPhase+"");//数字从大变小
                if(mTotpCountdownPhase>0.5){
                    gifFromResource.seekTo(400+(int)(30000-(30000*mTotpCountdownPhase)));
                    Log.i("DCZ2",400+(30000-(30000*mTotpCountdownPhase))+"");//数字从大变小
                }else if(mTotpCountdownPhase<0.01){
                    gifFromResource.seekTo(400);
                    Log.i("DCZ3",400+"");//数字从大变小
                }else {
                    gifFromResource.seekTo((int)(30000-(30000*mTotpCountdownPhase)));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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

    private void setViews() {
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
        findViewById(R.id.Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
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
        shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying()){
                }else {
                    MyApplication.type=1;
                    recreate();
                }
            }
        });
        bangzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, AuthenticatorActivity.class);
                startActivity(intent);
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

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        if(LockUtil.getPwdStatus(INSTANCE)==true){
            tv_suo.setText(R.string.main2);
        }else {
            tv_suo.setText(R.string.main3);
        }
       /* if(MyApplication.status==true){
            MyApplication.type=1;
            recreate();
        }*/
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
                        ActivityUtils.getInstance().popAllActivities();
                        Intent inten=new Intent(INSTANCE, LoginEmailActivity.class);
                        startActivity(inten);
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
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().exit_login(MyApplication.device).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        SharedPreferences sf2 = getSharedPreferences("user2",MODE_PRIVATE);
                        final String username = sf2.getString("username","");//第二个参数为默认值
                        final String token = sf2.getString("token","");
                        final String nickname = sf2.getString("nickname","");
                        if(token==null||token.equals("")){
                            Log.i("dcz","只有一个账号");
                            MyApplication.token=token;MyApplication.sf.edit().putString("token","").commit();
                            Intent intent=new Intent(INSTANCE, LoginEmailActivity.class);
                            startActivity(intent);
                            ActivityUtils.getInstance().popActivity(INSTANCE);
                        }else {
                            sf2.edit().putString("token","").commit();
                            Log.i("dcz","有两个账号");
                            MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                            MyApplication.nickname=nickname;MyApplication.sf.edit().putString("nickname",nickname).commit();
                            MyApplication.username=username;MyApplication.sf.edit().putString("username",username).commit();
                            setViews();
                            setListener();
                        }
                    } else {
                        if(!response.body().getCode().equals("20003")){
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }
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
    }
}
