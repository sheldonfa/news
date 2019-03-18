package com.mypro.news.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页的pageBean
 **/
public class PageBean implements Serializable {

    private  Integer page = 1; //当前页
    private  Integer pageSize = 15 ; //每页显示的条数
    private  Integer totalCount ; // 总条数
    private  Integer totalPage ; //总页数
    private List<News> newsList ; //每页的数据

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        Double pageNumber = Math.ceil((double) this.getTotalCount() / this.getPageSize());
        return pageNumber.intValue();
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                ", newsList=" + newsList +
                '}';
    }
}
