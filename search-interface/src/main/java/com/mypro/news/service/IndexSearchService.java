package com.mypro.news.service;

import com.mypro.news.pojo.News;
import com.mypro.news.pojo.PageBean;
import com.mypro.news.pojo.SearchVo;

import java.util.List;

public interface IndexSearchService {
    List<News> findByKeywords(String keywords) throws Exception;

    PageBean page(SearchVo searchVo) throws Exception;
}
