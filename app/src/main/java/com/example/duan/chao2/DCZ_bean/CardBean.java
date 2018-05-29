package com.example.duan.chao2.DCZ_bean;

import java.util.List;

/**
 * Created by DELL on 2018/4/26.
 */

public class CardBean {

    /**
     * code : 20000
     * msg : succeed
     * desc : 成功
     * data : [{"code":"0","value":"ID"},{"code":"2","value":"PASSPORT"},{"code":"3","value":"Mainland travel permit for Hong Kong and Macau residents"},{"code":"4","value":"Mainland travel permit for Taiwan residents"}]
     * ok : true
     */

    private String code;
    private String msg;
    private String desc;
    private boolean ok;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * code : 0
         * value : ID
         */

        private String code;
        private String value;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
