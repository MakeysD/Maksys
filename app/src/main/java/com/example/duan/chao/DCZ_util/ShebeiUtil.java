package com.example.duan.chao.DCZ_util;

import android.content.Context;
import android.telephony.TelephonyManager;

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
}
