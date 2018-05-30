package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2017/7/17.
 */

public class CityBean {
    private int country_id;
    private int country_code;
    private String country_name_en;//英文
    private String country_name_cn;//中文
    private String country_name_tai;//泰文
    private String ab;

    public String getCountry_name_tai() {
        return country_name_tai;
    }

    public void setCountry_name_tai(String country_name_tai) {
        this.country_name_tai = country_name_tai;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getAb() {
        return ab;
    }

    public void setAb(String ab) {
        this.ab = ab;
    }

    public String getCountry_name_cn() {
        return country_name_cn;
    }

    public void setCountry_name_cn(String country_name_cn) {
        this.country_name_cn = country_name_cn;
    }

    public String getCountry_name_en() {
        return country_name_en;
    }

    public void setCountry_name_en(String country_name_en) {
        this.country_name_en = country_name_en;
    }
}
