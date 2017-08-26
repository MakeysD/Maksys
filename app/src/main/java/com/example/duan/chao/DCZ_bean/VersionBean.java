package com.example.duan.chao.DCZ_bean;

/**
 * Created by DELL on 2017/8/26.
 */

public class VersionBean extends LoginBean<VersionBean>{
        /**
         * newVersionFlg : 1
         * neededUpdated : 1
         * path : http://www.baidu.com
         * latestVersion : 1.0.1
         */

        private String newVersionFlg;
        private String neededUpdated;
        private Object path;
        private String latestVersion;

        public String getNewVersionFlg() {
            return newVersionFlg;
        }

        public void setNewVersionFlg(String newVersionFlg) {
            this.newVersionFlg = newVersionFlg;
        }

        public String getNeededUpdated() {
            return neededUpdated;
        }

        public void setNeededUpdated(String neededUpdated) {
            this.neededUpdated = neededUpdated;
        }

        public Object getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getLatestVersion() {
            return latestVersion;
        }

        public void setLatestVersion(String latestVersion) {
            this.latestVersion = latestVersion;
        }
}
