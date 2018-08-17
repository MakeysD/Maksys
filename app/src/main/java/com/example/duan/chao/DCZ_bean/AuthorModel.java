package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2018/8/8.
 */

public class AuthorModel {

    private String code;
    private String msg;
    private String desc;
    private WebAuthorBean data;
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

    public WebAuthorBean getData() {
        return data;
    }

    public void setData(WebAuthorBean data) {
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
