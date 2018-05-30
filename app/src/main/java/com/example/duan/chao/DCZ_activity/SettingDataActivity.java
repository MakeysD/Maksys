package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.jia.TimePickerView2;
import com.bigkoo.pickerview.jia.TimePickerView3;
import com.example.duan.chao.DCZ_ImageUtil.CompressHelper;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CardBean;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.ProvinceBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.ContentUtil;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingDataActivity extends BaseActivity {
    private SettingDataActivity INSTANCE;
    private String format="2018-12-31";
    private String sign;
    private Boolean select=false;
    private String content;
    private Dialog dialog;
    private String type="1";    //1:正面，2：反面，3：手持
    private File photo1;
    private File photo2;
    private File photo3;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public static CityBean bean;
    //调用照相机返回图片临时文件
    private File tempFile;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    TimePickerView pvTime;
    TimePickerView2 pvTime2;
    TimePickerView3 pvTime3;
    OptionsPickerView pvOptions;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;
    /*@BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.time)
    LinearLayout time;*/
    @BindView(R.id.ll_start)
    LinearLayout ll_start;
    @BindView(R.id.time1)
    TextView time1;
    @BindView(R.id.ll_end)
    LinearLayout ll_end;
    @BindView(R.id.time2)
    TextView time2;
    @BindView(R.id.ll1)
    LinearLayout ll1;       //国家地区
    @BindView(R.id.ll2)
    LinearLayout ll2;       //证件类型
    @BindView(R.id.type)
    TextView Type;
    @BindView(R.id.ll3)
    LinearLayout ll3;       //姓名
    @BindView(R.id.ll4)
    LinearLayout ll4;       //有效期
    @BindView(R.id.xian1)
    TextView xian1;
    @BindView(R.id.xian2)
    TextView xian2;
    @BindView(R.id.xian3)
    TextView xian3;
    @BindView(R.id.xian4)
    TextView xian4;
    @BindView(R.id.xian5)
    TextView xian5;
    @BindView(R.id.xian7)
    TextView xian7;
    @BindView(R.id.ll6)
    LinearLayout ll6;       //生日选择
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.xian6)
    TextView xian6;
    @BindView(R.id.et_name)
    TextView et_name;
    @BindView(R.id.et_number)
    EditText et_number;
    @BindView(R.id.tv_guo)
    TextView tv_guo;
    @BindView(R.id.rl_zheng)
    SimpleDraweeView zheng;   //身份证正面
    @BindView(R.id.rl_fan)
    SimpleDraweeView fan;
    @BindView(R.id.rl_shou)
    SimpleDraweeView shou;
    @BindView(R.id.x1)
    ImageView x1;
    @BindView(R.id.x2)
    ImageView x2;
    @BindView(R.id.x3)
    ImageView x3;
    @BindView(R.id.btn_camera)
    TextView camera;            //拍照
    @BindView(R.id.btn_photo)
    TextView photo;             //相册
    @BindView(R.id.pup)
    RelativeLayout pup;        //弹框
    @BindView(R.id.pup2)
    LinearLayout pup2;         //弹框里面的内容
    @BindView(R.id.tan)
    LinearLayout tan;          //证件解释
    @BindView(R.id.x)
    ImageView x;
    @BindView(R.id.pop2)
    LinearLayout pop2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCameraTempFile(savedInstanceState);
        setContentView(R.layout.activity_setting_data);
        INSTANCE=this;
        ButterKnife.bind(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                format=getNetTime();
            }
        }).start();
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        ShebeiUtil.setEdNoChinaese(et_number);
        if(MyApplication.language.equals("ENGLISH")){
            zheng.setBackgroundResource(R.mipmap.shenfenzhengen);
            fan.setBackgroundResource(R.mipmap.shenfenzheng2en);
            shou.setBackgroundResource(R.mipmap.shenfenzheng3en);
        }else if(MyApplication.language.equals("TAI")){
            zheng.setBackgroundResource(R.mipmap.shenfenzhengtai);
            fan.setBackgroundResource(R.mipmap.shenfenzheng2tai);
            shou.setBackgroundResource(R.mipmap.shenfenzheng3tai);
        }else if(MyApplication.language.equals("CHINESE_TW")){

        }else if(MyApplication.language.equals("SK")){

        }else if(MyApplication.language.equals("VI")){

        }else if(MyApplication.language.equals("")){
            if(MyApplication.xitong.equals("en_US")||MyApplication.xitong.equals("en_GB")){
                zheng.setBackgroundResource(R.mipmap.shenfenzhengen);
                fan.setBackgroundResource(R.mipmap.shenfenzheng2en);
                shou.setBackgroundResource(R.mipmap.shenfenzheng3en);
            }else if(MyApplication.xitong.equals("th_TH")){
                zheng.setBackgroundResource(R.mipmap.shenfenzhengtai);
                fan.setBackgroundResource(R.mipmap.shenfenzheng2tai);
                shou.setBackgroundResource(R.mipmap.shenfenzheng3tai);
            }else {
                zheng.setBackgroundResource(R.mipmap.shenfenzheng);
                fan.setBackgroundResource(R.mipmap.shenfenzheng2);
                shou.setBackgroundResource(R.mipmap.shenfenzheng3);
            }
        }else {
            zheng.setBackgroundResource(R.mipmap.shenfenzheng);
            fan.setBackgroundResource(R.mipmap.shenfenzheng2);
            shou.setBackgroundResource(R.mipmap.shenfenzheng3);
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        setViews();
        setListener();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }

    /**
     *  初始化
     * */
    private void setViews() {
        //setTime();
        getCard();
        setBirthday();
        set_end();
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.i("dcz_比较当前版本与服务器",time2.getText().toString().compareTo(time1.getText().toString())+"a");
                if(format!=null){
                    if(format.compareTo(time1.getText().toString())>0){//判断当前时间是否大于开始时间
                        if(time2.getText().toString().compareTo(time1.getText().toString())>0){//判断结束时间是否大于开始时间
                        }else {
                            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi119a),R.style.registDialog).show();
                            return;
                        }
                    }else {
                        new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi119a),R.style.registDialog).show();
                        return;
                    }
                }*/
                if(photo1!=null&&photo2!=null&&photo3!=null&&!tv_guo.getText().toString().equals(INSTANCE.getString(R.string.tishi124))&&!Type.getText().equals(getString(R.string.tishi123))&&et_name.getText().length()>0&&
                        et_number.getText().length()>0&&!time1.getText().equals(getString(R.string.tishi122a))&&!time2.getText().equals(getString(R.string.tishi122b))){
                    File x=null;    File y=null;    File z=null;
                    if(photo1.length()/1024>1000){
                        x = CompressHelper.getDefault(getApplicationContext()).compressToFile(photo1);
                    }else {
                        x =photo1;
                    }
                    if(photo2.length()/1024>1000){
                        y = CompressHelper.getDefault(getApplicationContext()).compressToFile(photo2);
                    }else {
                        y =photo2;
                    }
                    if(photo3.length()/1024>1000){
                        z = CompressHelper.getDefault(getApplicationContext()).compressToFile(photo3);
                    }else {
                        z = photo3;
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"),x);
                    RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/png"),y);
                    RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/png"),z);
                    Log.i("dcz:photo压缩前大小：",photo1.length()/1024+"KB");
                    Log.i("dcz:压缩后大小：",x.length()/1024+"KB");
                    Log.i("dcz:photo压缩前大小：",photo2.length()/1024+"KB");
                    Log.i("dcz:压缩后大小：",y.length()/1024+"KB");
                    Log.i("dcz:photo压缩前大小：",photo3.length()/1024+"KB");
                    Log.i("dcz:压缩后大小：",z.length()/1024+"KB");
                    MultipartBody.Part a = MultipartBody.Part.createFormData("frontFile",x.getName(),requestBody);
                    MultipartBody.Part b = MultipartBody.Part.createFormData("reverseFile",y.getName(),requestBody2);
                    MultipartBody.Part c = MultipartBody.Part.createFormData("holdingFile",z.getName(),requestBody3);
                    getData(getType(Type.getText().toString()),a,b,c,MyApplication.code);
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi119),R.style.registDialog).show();
                }
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type1();
                Intent intent=new Intent(INSTANCE,CityListActivity.class);
                startActivity(intent);
            }
        });

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(select==true){
                    type3();
                }else {
                    select=true;
                }
            }
        });
        et_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                type4();
            }
        });
        et_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type4();
            }
        });

        zheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="1";
                tan();
            }
        });
        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="2";
                tan();
            }
        });
        shou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="3";
                tan();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quan();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到调用系统图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
                shou();
            }
        });
        pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shou();
            }
        });
        x1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zheng.setImageURI(null);
                photo1=null;
                x1.setVisibility(View.GONE);
            }
        });
        x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fan.setImageURI(null);
                photo2=null;
                x2.setVisibility(View.GONE);
            }
        });
        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shou.setImageURI(null);
                photo3=null;
                x3.setVisibility(View.GONE);
            }
        });
        tan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tan2();
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shou2();
            }
        });
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(INSTANCE.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                pvTime2.show();
                type5();
            }
        });
        ll_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(INSTANCE.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                pvTime3.show();
                type7();
            }
        });
    }

    private void setTime(){
        //时间选择器
        /*pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        *//*Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦*//*
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,Date date2) {
                tv_time.setText(getTime(date2)+"   to  "+getTime(date));
                if(date2.getTime()>=date.getTime()||date2.getTime()>new Date().getTime()||date.getTime()<new Date().getTime()){
                    tv_time.setTextColor(Color.RED);
                }else {
                    tv_time.setTextColor(Color.WHITE);
                }
            }
        });
        //弹出时间选择器
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(INSTANCE.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                type5();
                pvTime.show();
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type2();
            }
        });*/
    }

    private void setBirthday(){
        //时间选择器
        pvTime2 = new TimePickerView2(this, TimePickerView2.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        pvTime2.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime2.setTime(new Date());
        pvTime2.setCyclic(true);
        pvTime2.setCancelable(true);
        //时间选择后回调
        pvTime2.setOnTimeSelectListener(new TimePickerView2.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                time1.setText(getTime(date));
                time1.setTextColor(Color.WHITE);
            }
        });
        //弹出时间选择器
        ll6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(INSTANCE.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                type6();
                pvTime3.show();
            }
        });
    }
    private void set_end(){
        //时间选择器
        pvTime3 = new TimePickerView3(this, TimePickerView3.Type.YEAR_MONTH_DAY);
        pvTime3.setTime(new Date());
        pvTime3.setCyclic(true);
        pvTime3.setCancelable(true);
        //时间选择后回调
        pvTime3.setOnTimeSelectListener(new TimePickerView3.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Log.i("qwe",date+"");
                if(getTime(date).equals("9999-12-31")){
                    time2.setText(getString(R.string.tishi158));
                    time2.setTextColor(Color.WHITE);
                }else {
                    time2.setText(getTime(date));
                    time2.setTextColor(Color.WHITE);
                }
            }
        });
    }

    private void setPicker(List<CardBean.DataBean> data){
        options1Items.clear();
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //选项1
        for(CardBean.DataBean b:data){
            options1Items.add(new ProvinceBean(0,b.getValue(),"",""));
            Log.i("qqq",b.getValue());
        }

        //options1Items.add(new ProvinceBean(2,INSTANCE.getString(R.string.tishi126),"",""));
       // options1Items.add(new ProvinceBean(3,"港澳通行证","",""));
       // options1Items.add(new ProvinceBean(4,INSTANCE.getString(R.string.tishi127),"",""));
        //三级联动效果
        pvOptions.setPicker(options1Items);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        //   pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, true, true);
        pvOptions.setCancelable(true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText();
                Type.setText(tx);Type.setTextColor(Color.WHITE);
            }
        });
        //点击弹出选项选择器
        ll2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(INSTANCE.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (tv_guo.getText().toString().equals(INSTANCE.getString(R.string.tishi124))){
                    ContentUtil.makeToast(INSTANCE,INSTANCE.getString(R.string.tishi155));
                    return;
                }
                pvOptions.show();
            }
        });
    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private String getNetTime() {
        URL url = null;//取得资源对象
        String format=null;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            format = formatter.format(calendar.getTime());
            Log.i("dcz网络时间：",format+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format;
    }
    private void getBJTime(){
        Locale locale=Locale.CHINA; //这是获得本地中国时区
        String pattern = "yyyy-MM-dd";//这是日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern,locale);//设定日期格式
        Date date = new Date();
        URL url= null;//取得资源对象
        try {
            url = new URL("http://www.bjtime.cn");
            URLConnection uc=url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld=uc.getDate(); //取得网站日期时间
            date=new Date(ld); //转换为标准时间对象
            String bjTime = df.format(date);
            Log.i("dcz北京时间:",bjTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MyApplication.city.equals("")){
        }else {
            tv_guo.setText(MyApplication.city);
            tv_guo.setTextColor(Color.WHITE);
        }
    }
    private void type1(){
        xian1.setBackgroundColor(Color.parseColor("#0581c6"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
        xian6.setBackgroundColor(Color.parseColor("#343436"));
        xian7.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type2(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#0581c6"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
        xian6.setBackgroundColor(Color.parseColor("#343436"));
        xian7.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type3(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#0581c6"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
        xian6.setBackgroundColor(Color.parseColor("#343436"));
        xian7.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type4(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#0581c6"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
        xian6.setBackgroundColor(Color.parseColor("#343436"));
        xian7.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type5(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#0581c6"));
        xian6.setBackgroundColor(Color.parseColor("#343436"));
        xian7.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type6(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
        xian6.setBackgroundColor(Color.parseColor("#0581c6"));
        xian7.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type7(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
        xian7.setBackgroundColor(Color.parseColor("#0581c6"));
        xian6.setBackgroundColor(Color.parseColor("#343436"));
    }
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                Log.i("dcz",resultCode+"");
                if (resultCode == RESULT_OK) {
                    /*Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));*/
                    //file:///storage/emulated/0/image/1516352876304.jp
                    Uri uri = Uri.fromFile(tempFile);
                    Log.i("dcz",  Uri.fromFile(tempFile)+"qqq");
                    switch (type){
                        case "1":
                            photo1=tempFile;
                            if(photo1.length()/1024>5000){
                                photo1=null;
                                ContentUtil.makeToast(INSTANCE,getString(R.string.tishi161));
                            }else {
                                zheng.setImageURI(uri);
                                x1.setVisibility(View.VISIBLE);
                            }
                            break;
                        case "2":
                            photo2=tempFile;
                            if(photo2.length()/1024>5000){
                                photo2=null;
                                ContentUtil.makeToast(INSTANCE,getString(R.string.tishi161));
                            }else {
                                fan.setImageURI(uri);
                                x2.setVisibility(View.VISIBLE);
                            }
                            break;
                        case "3":
                            photo3=tempFile;
                            if(photo3.length()/1024>5000){
                                photo3=null;
                                ContentUtil.makeToast(INSTANCE,getString(R.string.tishi161));
                            }else {
                                shou.setImageURI(uri);
                                x3.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Log.i("dcz","doing");
                    Uri uri = data.getData();
                    Log.i("dcz",uri+"qqq");
                    switch (type){
                        case "1":
                           /* photo1=getFile(uri);
                            File x = CompressHelper.getDefault(getApplicationContext()).compressToFile(photo1);
                            Log.i("dcz:photo压缩前大小：",photo1.length()/1024+"KB");
                            Log.i("dcz:压缩后大小：",x.length()/1024+"KB");
                            Bitmap bit = BitmapFactory.decodeFile(x.getAbsolutePath());
                            Uri ur = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bit, null,null));
                            zheng.setImageURI(ur);*/
                            photo1=getFile(uri);
                            Log.i("大小",photo1.length()/1024+"23");
                            if(photo1.length()/1024>5000){
                                photo1=null;
                                ContentUtil.makeToast(INSTANCE,getString(R.string.tishi161));
                            }else {
                                zheng.setImageURI(uri);
                                x1.setVisibility(View.VISIBLE);
                            }
                            break;
                        case "2":
                            photo2=getFile(uri);
                            if(photo2.length()/1024>5000){
                                photo2=null;
                                ContentUtil.makeToast(INSTANCE,getString(R.string.tishi161));
                                return;
                            }else {
                                fan.setImageURI(uri);
                                x2.setVisibility(View.VISIBLE);
                            }
                            break;
                        case "3":
                            photo3=getFile(uri);
                            if(photo3.length()/1024>5000){
                                photo3=null;
                                ContentUtil.makeToast(INSTANCE,getString(R.string.tishi161));
                                return;
                            }else {
                                shou.setImageURI(uri);
                                x3.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                }
                break;
        }
    }

    //手动开启相机权限
    private void quan(){
        if(ContextCompat.checkSelfPermission(INSTANCE, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            //跳转到调用系统相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(intent, REQUEST_CAPTURE);
            shou();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限获取成功
                Log.i("dcz","权限获取成功");
                //跳转到调用系统相机
          /*      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAPTURE);
                shou();*/
            }else{
                //权限被拒绝
                Log.i("dcz","权限被拒绝");
            }
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }
    private void tan2(){
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        a.setDuration(300);
        a.setInterpolator(new LinearInterpolator());
        pop2.startAnimation(a);
        pop2.setVisibility(View.VISIBLE);
    }
    private void tan(){
        pup.setVisibility(View.VISIBLE);
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        a.setDuration(300);
        a.setInterpolator(new LinearInterpolator());
        pup2.startAnimation(a);
        pup2.setVisibility(View.VISIBLE);
    }
    private void shou(){
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        a.setDuration(300);
        a.setInterpolator(new LinearInterpolator());
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pup2.setVisibility(View.GONE);
                pup.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pup2.startAnimation(a);
    }
    private void shou2(){
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        a.setDuration(300);
        a.setInterpolator(new LinearInterpolator());
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pop2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pop2.startAnimation(a);
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(Integer type,MultipartBody.Part x,MultipartBody.Part y,MultipartBody.Part z,String code){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog.show();
       /* String[]  strs= tv_time.getText().toString().split("to");
        Log.i("dcz",strs[0].trim()); Log.i("dcz",strs[1].trim());*/
        String max= RandomUtil.RandomNumber();
        String end_time=null;
        end_time=time2.getText().toString();
        if(end_time.equals(getString(R.string.tishi158))){
            end_time="9999-12-31";
        }
        String str ="certNum="+et_number.getText().toString()+"&certType="+type+"&countryCode="+code+"&nonce="+max+"&realName="+et_name.getText().toString()+"&systemId="+"2001"+"&validityEnd="+end_time.trim()+"&validityStart="+time1.getText().toString().trim();
        byte[] data = str.getBytes();
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpServiceClient.getInstance().UserInfo(x,y,z,code,type,et_name.getText().toString(),et_number.getText().toString(),time1.getText().toString(),end_time,"2001",max,sign).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("10516")){
                            MyApplication.sf.edit().putString("cookie","").commit();
                            MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                            MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                            new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                                @Override
                                public void onActivieButtonClick(Object bean, int position) {
                                    ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                    ActivityUtils.getInstance().popAllActivities();
                                }
                            }, R.style.registDialog).show();
                            return;
                        }
                        if(response.body().getCode().equals("20000")){
                            Intent intent=new Intent(INSTANCE,PersonDataActivity.class);
                            startActivity(intent);
                            ActivityUtils.getInstance().popActivity(INSTANCE);
                        }else {
                            if(!response.body().getCode().equals("20003")){
                                new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
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
            public void onFailure(Call<LoginBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SettingDataActivity){
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }

    public void getCard(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        HttpServiceClient.getInstance().getIDCard(null).enqueue(new Callback<CardBean>() {
            @Override
            public void onResponse(Call<CardBean> call, Response<CardBean> response) {
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        setPicker( response.body().getData());
                    }else {
                        new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                    }
                }else {
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                }
            }
            @Override
            public void onFailure(Call<CardBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof LoginEmailActivity){
                    dialog.dismiss();
                    Log.i("dcz异常",call.toString()+"异常");
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    private File getFile(Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        File file = new File(img_path);
        return file;
    }
    private Integer getType(String string){
        Integer a = null;
        if(string.equals(INSTANCE.getString(R.string.tishi126))){
            a=2;
        }else {
            a=0;
        }
        return a;
    }
}
