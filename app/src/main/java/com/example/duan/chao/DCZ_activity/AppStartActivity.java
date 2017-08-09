package com.example.duan.chao.DCZ_activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 *  APP启动页
 *
 * */
public class AppStartActivity extends Activity {
    private int[] mIndexs;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        quan();
    }

    private void suo() {
        MyApplication.rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.i("dcz_设备ID", ShebeiUtil.getDeviceId(this));
        Log.i("dcz_设备md5", md5(ShebeiUtil.getDeviceId(this)));
        Log.i("dcz_设备型号",ShebeiUtil.getPhoneModel());
        Log.i("dcz_手机品牌",ShebeiUtil.getPhoneBrand());
        MyApplication.device=ShebeiUtil.getDeviceId(this);
        MyApplication.xinghao=ShebeiUtil.getPhoneModel();
        //判断是否登录
        if(MyApplication.token.equals("")){
            //判断是否设置过指纹锁
            if(MyApplication.zhiwen==true){
                Intent intent = new Intent(AppStartActivity.this, ZhiwenActivity.class);
                startActivity(intent);
                //判断当前是否设置过手势锁密码
            }else if(LockUtil.getPwdStatus(this)==true&& MyApplication.suo==true){
                Intent intent=new Intent(this,LoginLockActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                finish();
            }else {
                //判断是否是第一次登录
                if(MyApplication.first){
                    Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }else {
            Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();

        //mIndexs= LockUtil.getPwd(this);
    }

    private void quan(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            Log.i("dcz","执行3");
        } else {
            Log.i("dcz","执行2");
            suo();
        }
        GPS();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i("dcz","执行");
                    suo();
                }else {
                    finish();
                }
                break;
            default:
                break;
        }
    }


    private void GPS() {
        //获取定位服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        Log.i("dcz",location.toString());
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string = "纬度为：" + location.getLatitude() + ",经度为："
                    + location.getLongitude();
            Log.i("dcz",string);
            try {
                Log.i("dcz",getAddressFromLocation(this,location));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //绑定定位事件，监听位置是否改变
        //第一个参数为控制器类型第二个参数为监听位置变化的时间间隔（单位：毫秒）
        //第三个参数为位置变化的间隔（单位：米）第四个参数为位置监听器
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 2000, 2, locationListener);
    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            // 更新当前经纬度
        }
    };
    //关闭时解除监听器
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
    /**
     * 根据经纬度解码地理位置
     *
     * @param activity
     * @param location
     * @return
     */

    private static String getAddressFromLocation(final Activity activity, Location location) throws IOException {
        Geocoder geocoder = new Geocoder(activity);
        try {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("dcz", "getAddressFromLocation->lat:" + latitude + ", long:" + longitude);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                //返回当前位置，精度可调
                Address address = addresses.get(0);
                Log.i("dcz1",address.getCountryName());
                MyApplication.city=address.getCountryName();
                Log.i("dcz2",address.getFeatureName());
                Log.i("dcz3",address.getSubLocality());
                Log.i("dcz4",address.getAdminArea());
                address.getCountryName();
                String sAddress;
                if (!TextUtils.isEmpty(address.getLocality())) {
                    sAddress = address.getLocality();
                } else {
                    sAddress = "定位失败";
                }
                return sAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
