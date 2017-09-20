package com.example.duan.chao.DCZ_util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.NetworkInfo.State;

/**
 * Created by DELL on 2017/8/2.
 */

public class ShebeiUtil {
    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "";
        } else {
            return deviceId;
        }
    }
    /**
     * 限制edittext 不能输入中文
     * @param editText
     */
    public static void setEdNoChinaese(final EditText editText){
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                //注意返回值是char数组
                char[] stringArr = txt.toCharArray();
                for (int i = 0; i < stringArr.length; i++) {
                    //转化为string
                    String value = new String(String.valueOf(stringArr[i]));
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(value);
                    if (m.matches()) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                        return;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEdit(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }
    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

      /** 
       * 获取当前手机系统版本号 
       * 
       * @return  系统版本号 
       */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
     }
    public static String wang(Context context){
        String state="";
        State wifiState = null;
        State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if(wifiState!=null&&mobileState!=null&&State.CONNECTED!=wifiState&&State.CONNECTED==mobileState){// 手机网络连接成功  
            Log.i("网络改变","手机2g/3g/4g网络连接成功");
            state="1";
        }else if (wifiState!=null&&State.CONNECTED==wifiState){// 无线网络连接成功  
            Log.i("网络改变","无线网络连接成功");
            state="2";
        }else if (wifiState!=null&&mobileState!=null&&State.CONNECTED!=wifiState&&State.CONNECTED!=mobileState){// 手机没有任何的网络  
            Log.i("网络改变","手机没有任何的网络");
            state="0";
        }
        return state;
    }

    public static String ToString(InputStream is, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line).append("\n");
                }
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
