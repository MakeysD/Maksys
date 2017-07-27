package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  更换密保手机（身份证验证）
 *
 * */
public class ChangePhone3Activity extends BaseActivity {
    private ChangePhone3Activity INSTANCE;
    public static CityBean bean;
    public static String content="+86 ";
    private String type="1";    //1:正面，2：反面，3：手持
    //调用照相机返回图片临时文件
    private File tempFile;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    private Bundle savedInstanceState;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.button)
    View button;
    @BindView(R.id.city)
    TextView city;           //选择地区和国家
    @BindView(R.id.quhao)
    EditText quhao;
    @BindView(R.id.rl_zheng)
    SimpleDraweeView zheng;   //身份证正面
    @BindView(R.id.rl_fan)
    SimpleDraweeView fan;
    @BindView(R.id.rl_shou)
    SimpleDraweeView shou;
    @BindView(R.id.pup)
    RelativeLayout pup;        //弹框
    @BindView(R.id.pup2)
    LinearLayout pup2;         //弹框里面的内容
    @BindView(R.id.btn_camera)
    TextView camera;            //拍照
    @BindView(R.id.btn_photo)
    TextView photo;             //相册
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone3);
        this.savedInstanceState=savedInstanceState;
        ButterKnife.bind(this);
        INSTANCE=this;
        setViews();
        setListener();
    }

    /**
     *  数据初始化
     * */
    private void setViews() {

    }
    /**
     * 监听
     *
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, ChangePhone4Activity.class);
                startActivity(intent);
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(INSTANCE, CityListActivity.class);
                startActivity(intent);
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
                createCameraTempFile(savedInstanceState);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        city.setText(content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    Uri uri = Uri.fromFile(tempFile);
                    Log.i("Dcz",uri+"");
                    switch (type){
                        case "1":
                            zheng.setImageURI(uri);
                            break;
                        case "2":
                            fan.setImageURI(uri);
                            break;
                        case "3":
                            shou.setImageURI(uri);
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
                            break;
                        case "2":
                            fan.setImageURI(uri);
                            break;
                        case "3":
                            shou.setImageURI(uri);
                            break;
                    }
                }
                break;
        }
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

    private void quan(){
        if(ContextCompat.checkSelfPermission(INSTANCE, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(INSTANCE, new String[]{Manifest.permission.CAMERA}, 1);
        }else {
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, REQUEST_CAPTURE);
                shou();
            }else{
                //权限被拒绝
                Log.i("dcz","权限被拒绝");
            }
        }
    }
}
