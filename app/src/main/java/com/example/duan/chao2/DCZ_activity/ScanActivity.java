package com.example.duan.chao2.DCZ_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.example.duan.chao2.DCZ_application.MyApplication;
import com.example.duan.chao2.DCZ_bean.LoginOkBean;
import com.example.duan.chao2.DCZ_selft.MiddleDialog;
import com.example.duan.chao2.DCZ_util.ActivityUtils;
import com.example.duan.chao2.DCZ_util.ContentUtil;
import com.example.duan.chao2.DCZ_util.DSA;
import com.example.duan.chao2.DCZ_util.DensityUtils;
import com.example.duan.chao2.DCZ_util.DialogUtil;
import com.example.duan.chao2.DCZ_util.HttpServiceClient;
import com.example.duan.chao2.DCZ_util.RandomUtil;
import com.example.duan.chao2.DCZ_util.ScreenUtils;
import com.example.duan.chao2.DCZ_util.ShebeiUtil;
import com.example.duan.chao2.R;
import com.example.duan.chao2.zxing_code.android.CaptureActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.duan.chao2.DCZ_application.MyApplication.dialog;


/**
 *  扫码验证
 *
 * */
public class ScanActivity extends CaptureActivity {
    private String sign;
    private void getTicketInfo(String serial) {
        /*this.serial = serial;
        dialog.show();
        Log.i("dcz","扫二维码结果是："+serial);
        if(serial.contains("uuid")&&serial.contains("systemId")){
            String systemId = serial.substring(serial.indexOf("=")+1,serial.indexOf("&"));
            String uuid = serial.substring(serial.lastIndexOf("=")+1);
            Log.i("dcz","uuid结果："+uuid);
            Log.i("dcz","systemId："+systemId);
            MyApplication.reqSysId=systemId;
            if(serial!=null){
                getData(uuid);
            }
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }

    @Override
    public void addView() {

    }

    @Override
    public void initBarcodelay() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        String s="屏幕的分辨率为："+dm.widthPixels+"*"+dm.heightPixels;
        Log.i("dcz分辨率",s);
        int screenHeight = DensityUtils.px2dp(ScanActivity.this, ScreenUtils.getScreenHeight(ScanActivity.this));
        int recoderHeight = DensityUtils.px2dp(ScanActivity.this, ScreenUtils.getScreenWidth(ScanActivity.this)) / 6;
        getCameraManager().setPoint_left(recoderHeight);
        getCameraManager().setPoint_top((int) (screenHeight / 2 - recoderHeight*3));
        getCameraManager().setView_recoder_hight(recoderHeight*4);
        getViewfinderView().setmTipText(this.getString(R.string.scan_text));
        /*int height=240;
        getCameraManager().setPoint_left(50);
        getCameraManager().setPoint_top(screenHeight/2-height);
        getCameraManager().setView_recoder_hight(height);
        getViewfinderView().setmTipText(this.getString(R.string.scan_text));*/
    }

    @Override
    public void decodeResult(String serial, Bitmap barcode) {
        if(serial.contains("uuid")&&serial.contains("systemId")){
            dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"2");
            String systemId = serial.substring(serial.indexOf("=")+1,serial.indexOf("&"));
            String uuid = serial.substring(serial.lastIndexOf("=")+1);
            Log.i("dcz","扫码结果："+serial+"");
            Log.i("dcz","uuid结果："+uuid);
            Log.i("dcz","systemId："+systemId);
            MyApplication.reqSysId=systemId;
            if(serial!=null){
                getData(uuid);
            }
        }else {
            ContentUtil.makeToast(getApplicationContext(),getString(R.string.tishi128a));
        }
    }

    private void getData(String uuid){
        if(ShebeiUtil.wang(this).equals("0")){
            new MiddleDialog(this,this.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog.show();
        String max= RandomUtil.RandomNumber();
        String str ="nonce="+max+"&reqSysId=2001"+"&srcReqSysId="+MyApplication.reqSysId+"&username="+MyApplication.username+"&uuid="+uuid;
        byte[] data = str.getBytes();
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpServiceClient.getInstance().scan("2001",MyApplication.reqSysId,MyApplication.username, uuid,max,sign).enqueue(new Callback<LoginOkBean>() {
            @Override
            public void onResponse(Call<LoginOkBean> call, Response<LoginOkBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Log.d("dcz","获取数据成功");
                    if(response.body().getCode().equals("20000")){
                        Intent intent=new Intent(ScanActivity.this,HaveActivity.class);
                        MyApplication.reqFlowId=response.body().getData().getAuthzId();
                        Log.d("dcz_req",MyApplication.reqFlowId);
                        startActivity(intent);
                        ActivityUtils.getInstance().popActivity(ScanActivity.this);
                    }else {
                        if(response.body().getCode().equals("10516")){
                            MyApplication.sf.edit().putString("cookie","").commit();
                            MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                            MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                            new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),ScanActivity.this.getString(R.string.tishi101),ScanActivity.this.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                                @Override
                                public void onActivieButtonClick(Object bean, int position) {
                                    ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                    ActivityUtils.getInstance().popAllActivities();
                                }
                            }, R.style.registDialog).show();
                        }else if(response.body().getCode().equals("20003")){
                            MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        } else {
                            Toast.makeText(ScanActivity.this,MyApplication.map.get(response.body().getCode()).toString(), Toast.LENGTH_SHORT).show();
                            ActivityUtils.getInstance().popActivity(ScanActivity.this);
                        }
                    }
                }else {
                    Toast.makeText(ScanActivity.this,ScanActivity.this.getString(R.string.tishi116), Toast.LENGTH_SHORT).show();
                    ActivityUtils.getInstance().popActivity(ScanActivity.this);
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof ScanActivity){
                    dialog.dismiss();
                    Log.i("dcz异常",call.toString());
                    new MiddleDialog(ScanActivity.this,ScanActivity.this.getString(R.string.tishi72),R.style.registDialog).show();
                    ActivityUtils.getInstance().popActivity(ScanActivity.this);
                }
            }
        });
    }
}
