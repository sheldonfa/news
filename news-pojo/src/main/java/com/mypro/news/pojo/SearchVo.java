package com.mypro.news.pojo;

import java.io.Serializable;

public class SearchVo implements Serializable {
    private String keywords;
    private String editor;
    private String source;
    private String startDate;
    private String endDate;
    private PageBean pageBean;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    @Override
    public String toString() {
        return "SearchVo{" +
                "keywords='" + keywords + '\'' +
                ", editor='" + editor + '\'' +
                ", source='" + source + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", pageBean=" + pageBean +
                '}';
    }
}
