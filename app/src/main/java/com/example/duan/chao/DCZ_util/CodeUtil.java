package com.example.duan.chao.DCZ_util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.R;

import java.util.HashMap;

/**
 * Created by DELL on 2017/8/30.
 */

public class CodeUtil {
    Context context;
    public static void pushcode(Context context){
        MyApplication.map.clear();
        MyApplication.map.put("20000",context.getString(R.string.code1));
        MyApplication.map.put("20001",context.getString(R.string.code2));
        MyApplication.map.put("20002",context.getString(R.string.code3));
        MyApplication.map.put("20003",context.getString(R.string.code4));
        MyApplication.map.put("20004",context.getString(R.string.code5));
        MyApplication.map.put("20005",context.getString(R.string.code6));
        MyApplication.map.put("20007",context.getString(R.string.code7));
        MyApplication.map.put("10011",context.getString(R.string.code8));
        MyApplication.map.put("10013",context.getString(R.string.code9));
        MyApplication.map.put("10014",context.getString(R.string.code10));
        MyApplication.map.put("10015",context.getString(R.string.code11));
        MyApplication.map.put("10016",context.getString(R.string.code12));
        MyApplication.map.put("10017",context.getString(R.string.code13));
        MyApplication.map.put("10020",context.getString(R.string.code14));
        MyApplication.map.put("10021",context.getString(R.string.code15));
        MyApplication.map.put("10022",context.getString(R.string.code16));
        MyApplication.map.put("10024",context.getString(R.string.code17));
        MyApplication.map.put("10025",context.getString(R.string.code18));
        MyApplication.map.put("10026",context.getString(R.string.code19));
        MyApplication.map.put("10027",context.getString(R.string.code20));
        MyApplication.map.put("10028",context.getString(R.string.code21));
        MyApplication.map.put("10029",context.getString(R.string.code22));
        MyApplication.map.put("10033",context.getString(R.string.code23));
        MyApplication.map.put("10034",context.getString(R.string.code24));
        MyApplication.map.put("10036",context.getString(R.string.code25));
        MyApplication.map.put("10037",context.getString(R.string.code26));
        MyApplication.map.put("10038",context.getString(R.string.code27));
        MyApplication.map.put("10500",context.getString(R.string.code28));
        MyApplication.map.put("10501",context.getString(R.string.code29));
        MyApplication.map.put("10502",context.getString(R.string.code30));
        MyApplication.map.put("10503",context.getString(R.string.code31));
        MyApplication.map.put("10504",context.getString(R.string.code32));
        MyApplication.map.put("10505",context.getString(R.string.code33));
        MyApplication.map.put("10506",context.getString(R.string.code34));
        MyApplication.map.put("10508",context.getString(R.string.code35));
        MyApplication.map.put("10509",context.getString(R.string.code36));
        MyApplication.map.put("10510",context.getString(R.string.code37));

        MyApplication.map.put("0",context.getString(R.string.data0));
        MyApplication.map.put("1",context.getString(R.string.data1));
        MyApplication.map.put("2",context.getString(R.string.data2));
        MyApplication.map.put("3",context.getString(R.string.data3));
        MyApplication.map.put("4",context.getString(R.string.data4));
        MyApplication.map.put("5",context.getString(R.string.data5));
        MyApplication.map.put("6",context.getString(R.string.data6));
        MyApplication.map.put("7",context.getString(R.string.data7));
        MyApplication.map.put("8",context.getString(R.string.data8));
        MyApplication.map.put("9",context.getString(R.string.data9));
        MyApplication.map.put("10",context.getString(R.string.data10));
        MyApplication.map.put("11",context.getString(R.string.data11));
        MyApplication.map.put("12",context.getString(R.string.data12));
        MyApplication.map.put("13",context.getString(R.string.data13));
        MyApplication.map.put("14",context.getString(R.string.data14));
        MyApplication.map.put("15",context.getString(R.string.data15));
        MyApplication.map.put("16",context.getString(R.string.data16));
        MyApplication.map.put("17",context.getString(R.string.data17));
        MyApplication.map.put("18",context.getString(R.string.data18));
        MyApplication.map.put("19",context.getString(R.string.data19));
        MyApplication.map.put("1000",context.getString(R.string.data1000));
        MyApplication.map.put("1001",context.getString(R.string.data1001));
        MyApplication.map.put("1002",context.getString(R.string.data1002));
        MyApplication.map.put("1003",context.getString(R.string.data1003));
        MyApplication.map.put("1004",context.getString(R.string.data1004));
        MyApplication.map.put("1009",context.getString(R.string.data1009));
        MyApplication.map.put("2000",context.getString(R.string.data2000));

    }

    public String getCode(Context context,String string){
        String code = null;
        switch (string){
            case "20000":
                code=context.getString(R.string.code1);
                break;
        }
        return code;
    }

}