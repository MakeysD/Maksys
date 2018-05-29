package com.example.duan.chao2.DCZ_bean;


/**
 * Created by DELL on 2017/9/21.
 */

public class UserStateBean extends LoginBean<UserStateBean>{
    private String codeX;
    private String name;
    private Object description;

    public String getCodeX() {
        return codeX;
    }

    public void setCodeX(String codeX) {
        this.codeX = codeX;
    }

    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }
}
