package com.example.duan.chao.DCZ_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DensityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ScreenUtils;
import com.example.duan.chao.R;
import com.example.duan.chao.zxing_code.android.CaptureActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.duan.chao.DCZ_application.MyApplication.dialog;


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
        int screenHeight = DensityUtils.px2dp(ScanActivity.this, ScreenUtils.getScreenHeight(ScanActivity.this));
        int recoderHeight = DensityUtils.px2dp(ScanActivity.this, ScreenUtils.getScreenWidth(ScanActivity.this)) / 3;
        getCameraManager().setPoint_left(recoderHeight / 2);
        getCameraManager().setPoint_top((int) (screenHeight / 2 - recoderHeight*1.5));
        getCameraManager().setView_recoder_hight(recoderHeight*2);
        getViewfinderView().setmTipText(this.getString(R.string.scan_text));
    }

    @Override
    public void decodeResult(String serial, Bitmap barcode) {
        if(serial.contains("uuid")&&serial.contains("systemId")){
            dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"2");
            dialog.show();
            String systemId = serial.substring(serial.indexOf("=")+1,serial.indexOf("&"));
            String uuid = serial.substring(serial.lastIndexOf("=")+1);
            Log.i("dcz","uuid结果："+uuid);
            Log.i("dcz","systemId："+systemId);
            MyApplication.reqSysId=systemId;
            if(serial!=null){
                getData(uuid);
            }
        }
    }


    private void getData(String uuid){
        String max="8579996398";
        String str ="nonce="+max+"&reqSysId=2001"+"&srcReqSysId="+MyApplication.reqSysId+"&username="+MyApplication.username+"&uuid="+uuid;
        byte[] data = str.getBytes();
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpServiceClient.getInstance().scan("2001",MyApplication.reqSysId,MyApplication.username, uuid,"8579963998",sign).enqueue(new Callback<LoginOkBean>() {
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
                        // new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                        Toast.makeText(ScanActivity.this,MyApplication.map.get(response.body().getCode()).toString(), Toast.LENGTH_SHORT).show();
                        ActivityUtils.getInstance().popActivity(ScanActivity.this);
                    }
                }else {
                    // new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi83),R.style.registDialog).show();
                    Toast.makeText(ScanActivity.this,"网络异常", Toast.LENGTH_SHORT).show();
                    ActivityUtils.getInstance().popActivity(ScanActivity.this);
                }
            }
            @Override
            public void onFailure(Call<LoginOkBean> call, Throwable t) {
                dialog.dismiss();
                Log.i("dcz异常",call.toString());
                Toast.makeText(ScanActivity.this,"解析", Toast.LENGTH_SHORT).show();
                ActivityUtils.getInstance().popActivity(ScanActivity.this);
                //new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
