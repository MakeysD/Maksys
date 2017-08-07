package com.example.duan.chao.DCZ_bean;

import java.util.List;

/**
 * Created by DELL on 2017/7/14.
 */

public class FootprintsBean extends LoginBean<FootprintsBean>{
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
             * id : 1
             * userId : 1
             * type : 1
             * createTime : 1501511774000
             * username : admin
             * typeDesc : null
             * updateTime : 1501511773751
             * systemId : 2000
             * systemName : 用户安全中心
             * ipAddr : 192.168.58.1
             * ipAttr : null
             * os : null
             * browser : null
             * application : null
             */

            private int pageNumber;
            private int pageSize;
            private Object orderBy;
            private Object startTime;
            private Object endTime;
            private int id;
            private int userId;
            private int type;
            private long createTime;
            private String username;
            private Object typeDesc;
            private long updateTime;
            private String systemId;
            private String systemName;
            private String ipAddr;
            private Object ipAttr;
            private Object os;
            private Object browser;
            private Object application;

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

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public Object getTypeDesc() {
                return typeDesc;
            }

            public void setTypeDesc(Object typeDesc) {
                this.typeDesc = typeDesc;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getSystemId() {
                return systemId;
            }

            public void setSystemId(String systemId) {
                this.systemId = systemId;
            }

            public String getSystemName() {
                return systemName;
            }

            public void setSystemName(String systemName) {
                this.systemName = systemName;
            }

            public String getIpAddr() {
                return ipAddr;
            }

            public void setIpAddr(String ipAddr) {
                this.ipAddr = ipAddr;
            }

            public Object getIpAttr() {
                return ipAttr;
            }

            public void setIpAttr(Object ipAttr) {
                this.ipAttr = ipAttr;
            }

            public Object getOs() {
                return os;
            }

            public void setOs(Object os) {
                this.os = os;
            }

            public Object getBrowser() {
                return browser;
            }

            public void setBrowser(Object browser) {
                this.browser = browser;
            }

            public Object getApplication() {
                return application;
            }

            public void setApplication(Object application) {
                this.application = application;
            }
        }
}
