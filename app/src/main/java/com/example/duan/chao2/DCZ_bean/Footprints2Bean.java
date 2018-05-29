package com.example.duan.chao2.DCZ_bean;

import java.util.List;

/**
 * Created by DELL on 2017/7/14.
 */

public class Footprints2Bean {

    /**
     * code : 20000
     * msg : success
     * desc : 成功
     * data : [{"sessionId":"9fd0b98e-577e-49d2-a4b1-4bb50aa5986c","os":null,"browser":null,"username":"admin","ip":"192.168.2.111","systemId":"1002","systemName":"原力平台","creationTime":1502954367729,"lastAccessedTime":1502954367729}]
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
         * sessionId : 9fd0b98e-577e-49d2-a4b1-4bb50aa5986c
         * os : null
         * browser : null
         * username : admin
         * ip : 192.168.2.111
         * systemId : 1002
         * systemName : 原力平台
         * creationTime : 1502954367729
         * lastAccessedTime : 1502954367729
         */

        private String sessionId;
        private Object os;
        private Object browser;
        private Object username;
        private Object ip;
        private Object systemId;
        private Object systemName;
        private long creationTime;
        private long lastAccessedTime;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public Object getOs() {
            return os;
        }

        public void setOs(Object os) {
            this.os = os;
        }

        public Object getBrowser() {
            return browser;
        }

        public void setBrowser(Object browser) {
            this.browser = browser;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Object getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Object getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public Object getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }

        public long getLastAccessedTime() {
            return lastAccessedTime;
        }

        public void setLastAccessedTime(long lastAccessedTime) {
            this.lastAccessedTime = lastAccessedTime;
        }
    }
}
