package com.example.duan.chao2.DCZ_bean;

import java.util.List;

/**
 * Created by DELL on 2018/4/26.
 */

public class CountryBean {

    /**
     * code : string
     * msg : string
     * desc : string
     * ok : false
     * data : [{"countryCode":"string","countryName":"string","countryPinyin":"string","phoneCode":"string"}]
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
         * countryCode : string
         * countryName : string
         * countryPinyin : string
         * phoneCode : string
         */

        private String countryCode;
        private String countryName;
        private String countryPinyin;
        private String phoneCode;

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryPinyin() {
            return countryPinyin;
        }

        public void setCountryPinyin(String countryPinyin) {
            this.countryPinyin = countryPinyin;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }
    }
}
