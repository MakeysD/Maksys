package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2017/8/10.
 */

public class HaveBean {

    /**
     * businessName : 真的好吗df
     * reqFlowId : 4003
     * reqSysId : 1000
     * username : admin
     */

    private String businessName;
    private String reqFlowId;
    private String reqSysId;
    private String username;
    private String randomCode;
    private String srcReqSysId;
    private String loginTime;
    private String srcReqSysName;

    public String getSrcReqSysName() {
        return srcReqSysName;
    }

    public void setSrcReqSysName(String srcReqSysName) {
        this.srcReqSysName = srcReqSysName;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getSrcReqSysId() {
        return srcReqSysId;
    }

    public void setSrcReqSysId(String srcReqSysId) {
        this.srcReqSysId = srcReqSysId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getReqFlowId() {
        return reqFlowId;
    }

    public void setReqFlowId(String reqFlowId) {
        this.reqFlowId = reqFlowId;
    }

    public String getReqSysId() {
        return reqSysId;
    }

    public void setReqSysId(String reqSysId) {
        this.reqSysId = reqSysId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }
}
