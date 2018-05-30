package com.example.duan.chao.DCZ_bean;

import java.util.List;

/**
 * Created by duan on 2017/6/14.
 */

public class NewsBean {

    /**
     * msg : 获取信息成功!
     * code : 200
     * data : {"pageNum":1,"pageSize":10,"size":1,"startRow":1,"endRow":1,"total":1,"pages":1,"list":[{"id":"5280D9BC5C894D52A2542B5B08E7F97C","xxlxId":"5280D9BC5C894D52A2542B5B08E7F97C","xxlxTitle":"新闻动态","xxly":null,"title":"测试","fileName":"zx_1497425230900.jpg","fbr":null,"fbsj":"2017-06-13","yds":5,"content":null}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 1
         * startRow : 1
         * endRow : 1
         * total : 1
         * pages : 1
         * list : [{"id":"5280D9BC5C894D52A2542B5B08E7F97C","xxlxId":"5280D9BC5C894D52A2542B5B08E7F97C","xxlxTitle":"新闻动态","xxly":null,"title":"测试","fileName":"zx_1497425230900.jpg","fbr":null,"fbsj":"2017-06-13","yds":5,"content":null}]
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
             * id : 5280D9BC5C894D52A2542B5B08E7F97C
             * xxlxId : 5280D9BC5C894D52A2542B5B08E7F97C
             * xxlxTitle : 新闻动态
             * xxly : null
             * title : 测试
             * fileName : zx_1497425230900.jpg
             * fbr : null
             * fbsj : 2017-06-13
             * yds : 5
             * content : null
             */

            private String id;
            private String xxlxId;
            private String xxlxTitle;
            private Object xxly;
            private String title;
            private String fileName;
            private Object fbr;
            private String fbsj;
            private int yds;
            private Object content;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getXxlxId() {
                return xxlxId;
            }

            public void setXxlxId(String xxlxId) {
                this.xxlxId = xxlxId;
            }

            public String getXxlxTitle() {
                return xxlxTitle;
            }

            public void setXxlxTitle(String xxlxTitle) {
                this.xxlxTitle = xxlxTitle;
            }

            public Object getXxly() {
                return xxly;
            }

            public void setXxly(Object xxly) {
                this.xxly = xxly;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public Object getFbr() {
                return fbr;
            }

            public void setFbr(Object fbr) {
                this.fbr = fbr;
            }

            public String getFbsj() {
                return fbsj;
            }

            public void setFbsj(String fbsj) {
                this.fbsj = fbsj;
            }

            public int getYds() {
                return yds;
            }

            public void setYds(int yds) {
                this.yds = yds;
            }

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }
        }
    }
}
