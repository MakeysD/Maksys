package com.example.duan.chao2.DCZ_bean;

/**
 * Created by lizhaozhao on 16/4/26.
 */
public class StatusBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":4,"username":"test","picture":"http://192.168.2.189:8081/static/headImg/2d2d0b11b0134d238cf1d81b99847f05.png","nickname":"6yy","sex":"M","age":18,"area":"啊啊所大所大所多","height":150,"weight":11,"lastTime":"2017-07-27 19:39:03","token":"YmFhMzI5MGY4OTdjODI1MGI5MjA1ZDRjMjZkZDk4OTQ="}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4
         * username : test
         * picture : http://192.168.2.189:8081/static/headImg/2d2d0b11b0134d238cf1d81b99847f05.png
         * nickname : 6yy
         * sex : M
         * age : 18
         * area : 啊啊所大所大所多
         * height : 150
         * weight : 11
         * lastTime : 2017-07-27 19:39:03
         * token : YmFhMzI5MGY4OTdjODI1MGI5MjA1ZDRjMjZkZDk4OTQ=
         */

        private int id;
        private String username;
        private String picture;
        private String nickname;
        private String sex;
        private int age;
        private String area;
        private int height;
        private int weight;
        private String lastTime;
        private String token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
