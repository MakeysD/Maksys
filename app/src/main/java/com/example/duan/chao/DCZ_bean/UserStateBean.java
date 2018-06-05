package com.example.duan.chao.DCZ_bean;


/**
 * Created by DELL on 2017/9/21.
 */

public class UserStateBean extends LoginBean<UserStateBean>{
    private String codeX;
    private Integer step;
    private String name;
    private Object description;

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

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
