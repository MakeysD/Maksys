package com.example.duan.chao2.DCZ_bean;

/**
 * Created by DELL on 2017/8/3.
 */

public class LoginOkBean extends LoginBean<LoginOkBean>{

    private String refreshToken;
    private String nickname;
    private String username;
    private String userId;
    private String authzId;
    private String totpSecretKey;
    private String mobile;
    private int defaultIntervalInSecond;
    private int totpCodeLength;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getTotpCodeLength() {
        return totpCodeLength;
    }

    public void setTotpCodeLength(int totpCodeLength) {
        this.totpCodeLength = totpCodeLength;
    }

    public String getTotpSecretKey() {
        return totpSecretKey;
    }

    public void setTotpSecretKey(String totpSecretKey) {
        this.totpSecretKey = totpSecretKey;
    }

    public int getDefaultIntervalInSecond() {
        return defaultIntervalInSecond;
    }

    public void setDefaultIntervalInSecond(int defaultIntervalInSecond) {
        this.defaultIntervalInSecond = defaultIntervalInSecond;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthzId() {
        return authzId;
    }

    public void setAuthzId(String authzId) {
        this.authzId = authzId;
    }
}
