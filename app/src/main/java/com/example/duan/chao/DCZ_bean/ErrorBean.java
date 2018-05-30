package com.example.duan.chao.DCZ_bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lizhaozhao on 16/4/21.
 *
 * 请求错误类
 */
public class ErrorBean implements Parcelable {
    /**
     * status : error
     * error : {"code":"1012","error":"WRONG_PASSWORD","message":"密码错误!"}
     */

    public String code;
    public String error;
    public String message;

        public void setCode(String code) {
            this.code = code;
        }

        public void setError(String error) {
            this.error = error;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "code='" + code + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.error);
        dest.writeString(this.message);
    }

    public ErrorBean() {
    }

    protected ErrorBean(Parcel in) {
        this.code = in.readString();
        this.error = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<ErrorBean> CREATOR = new Parcelable.Creator<ErrorBean>() {
        public ErrorBean createFromParcel(Parcel source) {
            return new ErrorBean(source);
        }

        public ErrorBean[] newArray(int size) {
            return new ErrorBean[size];
        }
    };
}
