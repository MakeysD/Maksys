package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2017/11/17.
 */

public class TimeBean {

    private String code;
    private String msg;
    private String desc;
    private DataBean data;
    private boolean ok;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public static class DataBean {
        /**
         * millisecond : 1510899702368
         * defaultIntervalInSeccod : 30
         */

        private long millisecond;
        private int defaultIntervalInSeccod;
        private int totpCodeLength;

        public int getTotpCodeLength() {
            return totpCodeLength;
        }

        public void setTotpCodeLength(int totpCodeLength) {
            this.totpCodeLength = totpCodeLength;
        }

        public long getMillisecond() {
            return millisecond;
        }

        public void setMillisecond(long millisecond) {
            this.millisecond = millisecond;
        }

        public int getDefaultIntervalInSeccod() {
            return defaultIntervalInSeccod;
        }

        public void setDefaultIntervalInSeccod(int defaultIntervalInSeccod) {
            this.defaultIntervalInSeccod = defaultIntervalInSeccod;
        }
    }
}
