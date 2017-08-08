package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2017/8/3.
 */

public class HttpBean<T>{

    /**
     * code : 20000
     * msg : 登录成功！
     * desc : 成功
     * data : {"refreshToken":"f73b55b7ba6f12ade6f516878d812ff1"}
     * ok : true
     */

    private String code;
    private String msg;
    private String desc;
    private Object data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
