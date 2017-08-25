package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2017/8/3.
 */

public class LoginOkBean extends LoginBean<LoginOkBean>{

    private String refreshToken;
    private String nickname;
    private String username;
    private String userId;
    private String authzId;


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
