package com.example.duan.chao.DCZ_bean;

import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class EquipmentBean extends LoginBean<EquipmentBean>{
        /**
         * pageNum : 1
         * pageSize : 2
         * size : 2
         * orderBy : null
         * startRow : 0
         * endRow : 1
         * total : 2
         * pages : 1
         * list : [{"pageNumber":1,"pageSize":10,"orderBy":null,"startTime":null,"endTime":null,"id":6,"deviceId":"1234567890","username":"us","deviceName":null,"createTime":1501760419770,"updateTime":1501760419770,"gratTime":1501760419770,"lastLoginTime":1501760419770,"os":null,"client":null,"gratSysId":"2000","gratSysName":"用户安全中心","remark":null},{"pageNumber":1,"pageSize":10,"orderBy":null,"startTime":null,"endTime":null,"id":7,"deviceId":"866145033751781","username":"us","deviceName":"xiaomi","createTime":1501830831935,"updateTime":1501830831935,"gratTime":1501830831935,"lastLoginTime":1501830831935,"os":null,"client":null,"gratSysId":"2000","gratSysName":"用户安全中心","remark":null}]
         * firstPage : 1
         * prePage : 0
         * nextPage : 0
         * lastPage : 1
         * isFirstPage : true
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : [1]
         */

        private int pageNum;
        private int pageSize;
        private int size;
        private Object orderBy;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int firstPage;
        private int prePage;
        private int nextPage;
        private int lastPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Object getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(Object orderBy) {
            this.orderBy = orderBy;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            /**
             * pageNumber : 1
             * pageSize : 10
             * orderBy : null
             * startTime : null
             * endTime : null
             * id : 6
             * deviceId : 1234567890
             * username : us
             * deviceName : null
             * createTime : 1501760419770
             * updateTime : 1501760419770
             * gratTime : 1501760419770
             * lastLoginTime : 1501760419770
             * os : null
             * client : null
             * gratSysId : 2000
             * gratSysName : 用户安全中心
             * remark : null
             */

            private int pageNumber;
            private int pageSize;
            private Object orderBy;
            private Object startTime;
            private Object endTime;
            private int id;
            private String deviceId;
            private String username;
            private Object deviceName;
            private long createTime;
            private long updateTime;
            private long gratTime;
            private long lastLoginTime;
            private Object os;
            private Object client;
            private String gratSysId;
            private String gratSysName;
            private Object remark;

            public int getPageNumber() {
                return pageNumber;
            }

            public void setPageNumber(int pageNumber) {
                this.pageNumber = pageNumber;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public Object getOrderBy() {
                return orderBy;
            }

            public void setOrderBy(Object orderBy) {
                this.orderBy = orderBy;
            }

            public Object getStartTime() {
                return startTime;
            }

            public void setStartTime(Object startTime) {
                this.startTime = startTime;
            }

            public Object getEndTime() {
                return endTime;
            }

            public void setEndTime(Object endTime) {
                this.endTime = endTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public Object getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(Object deviceName) {
                this.deviceName = deviceName;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public long getGratTime() {
                return gratTime;
            }

            public void setGratTime(long gratTime) {
                this.gratTime = gratTime;
            }

            public long getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(long lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public Object getOs() {
                return os;
            }

            public void setOs(Object os) {
                this.os = os;
            }

            public Object getClient() {
                return client;
            }

            public void setClient(Object client) {
                this.client = client;
            }

            public String getGratSysId() {
                return gratSysId;
            }

            public void setGratSysId(String gratSysId) {
                this.gratSysId = gratSysId;
            }

            public String getGratSysName() {
                return gratSysName;
            }

            public void setGratSysName(String gratSysName) {
                this.gratSysName = gratSysName;
            }

            public Object getRemark() {
                return remark;
            }

            public void setRemark(Object remark) {
                this.remark = remark;
            }
        }
}
