package com.mypro.news.service;

import com.mypro.news.pojo.News;

import java.util.List;

public interface IndexWriterService {
    void saveNews(List<News> newsList) throws Exception;
}
