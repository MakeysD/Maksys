package com.example.duan.chao.DCZ_bean;

/**
 * Created by lizhaozhao on 16/4/26.
 */
public class StatusBean {
    /**
     * status : ok
     */

    private String status;
    private ErrorBean error;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }
}
