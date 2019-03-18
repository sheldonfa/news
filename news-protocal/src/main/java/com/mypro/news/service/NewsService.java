package com.mypro.news.service;

import com.mypro.news.pojo.PageBean;
import com.mypro.news.pojo.SearchVo;

public interface NewsService {

    void indexWriter() throws Exception;

    PageBean indexSearch(SearchVo search) throws Exception;
}
