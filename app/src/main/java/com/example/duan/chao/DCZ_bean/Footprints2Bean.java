package com.example.duan.chao.DCZ_bean;

import java.util.List;

/**
 * Created by DELL on 2017/7/14.
 */

public class Footprints2Bean {

    /**
     * code : string
     * msg : string
     * desc : string
     * ok : false
     * data : [{"browser":"string","creationTime":0,"ip":"string","lastAccessedTime":0,"os":"string","sessionId":"string","systemId":"string","systemName":"string","username":"string"}]
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
         * browser : string
         * creationTime : 0
         * ip : string
         * lastAccessedTime : 0
         * os : string
         * sessionId : string
         * systemId : string
         * systemName : string
         * username : string
         */

        private String browser;
        private int creationTime;
        private String ip;
        private int lastAccessedTime;
        private String os;
        private String sessionId;
        private String systemId;
        private String systemName;
        private String username;

        public String getBrowser() {
            return browser;
        }

        public void setBrowser(String browser) {
            this.browser = browser;
        }

        public int getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(int creationTime) {
            this.creationTime = creationTime;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getLastAccessedTime() {
            return lastAccessedTime;
        }

        public void setLastAccessedTime(int lastAccessedTime) {
            this.lastAccessedTime = lastAccessedTime;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
