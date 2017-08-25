package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_bean.ProvinceBean;
import com.example.duan.chao.DCZ_selft.CanRippleLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingDataActivity extends BaseActivity {
    private SettingDataActivity INSTANCE;
    private String type="1";    //1:正面，2：反面，3：手持
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
    OptionsPickerView pvOptions;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.time)
    LinearLayout time;
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
    @BindView(R.id.et_name)
    TextView et_name;
    @BindView(R.id.et_number)
    TextView et_number;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_data);
        INSTANCE=this;
        ButterKnife.bind(this);
        CanRippleLayout.Builder.on(button).rippleCorner(MyApplication.dp2Px()).create();
        tv_guo.setText(MyApplication.city);
        setViews();
        setListener();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        setTime();
        setPicker();
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
                Intent intent=new Intent(INSTANCE,SettingDataResultActivity.class);
                startActivity(intent);
                ActivityUtils.getInstance().popActivity(INSTANCE);
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
                type3();
            }
        });
        et_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
                x1.setVisibility(View.GONE);
            }
        });
        x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fan.setImageURI(null);
                x2.setVisibility(View.GONE);
            }
        });
        x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shou.setImageURI(null);
                x3.setVisibility(View.GONE);
            }
        });

    }

    private void setTime(){
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date,Date date2) {
                tv_time.setText(getTime(date2)+"   to  "+getTime(date));
            }
        });
        //弹出时间选择器
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                type5();
                pvTime.show();
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type2();
            }
        });
    }
    private void setPicker(){
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //选项1
        options1Items.add(new ProvinceBean(0,"身份证","",""));
        options1Items.add(new ProvinceBean(1,"护照","",""));
        options1Items.add(new ProvinceBean(3,"港澳通行证","",""));
        options1Items.add(new ProvinceBean(4,"学生证","",""));
        options1Items.add(new ProvinceBean(5,"驾照","",""));
        //三级联动效果
        pvOptions.setPicker(options1Items);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        //   pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText();
                Type.setText(tx);

            }
        });
        //点击弹出选项选择器
        ll2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });
    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_guo.setText(MyApplication.city);
    }

    private void type1(){
        xian1.setBackgroundColor(Color.parseColor("#0581c6"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type2(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#0581c6"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type3(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#0581c6"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type4(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#0581c6"));
        xian5.setBackgroundColor(Color.parseColor("#343436"));
    }
    private void type5(){
        xian1.setBackgroundColor(Color.parseColor("#343436"));
        xian2.setBackgroundColor(Color.parseColor("#343436"));
        xian3.setBackgroundColor(Color.parseColor("#343436"));
        xian4.setBackgroundColor(Color.parseColor("#343436"));
        xian5.setBackgroundColor(Color.parseColor("#0581c6"));
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
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
                    Log.i("dcz",uri+"");
                    switch (type){
                        case "1":
                            zheng.setImageURI(uri);
                            x1.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            fan.setImageURI(uri);
                            x2.setVisibility(View.VISIBLE);
                            break;
                        case "3":
                            shou.setImageURI(uri);
                            x3.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Log.i("dcz","doing");
                    Uri uri = data.getData();
                    switch (type){
                        case "1":
                            zheng.setImageURI(uri);
                            x1.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            fan.setImageURI(uri);
                            x2.setVisibility(View.VISIBLE);
                            break;
                        case "3":
                            shou.setImageURI(uri);
                            x3.setVisibility(View.VISIBLE);
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

}
