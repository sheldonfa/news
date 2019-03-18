package com.mypro.news.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mypro.news.pojo.News;
import com.mypro.news.service.IndexWriterService;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = IndexWriterService.class)
public class SolrIndexWriterServiceImpl implements IndexWriterService {

    @Autowired
    private SolrServer solrServer;

    @Override
    public void saveNews(List<News> newsList) throws Exception {
        if (newsList != null && newsList.size() > 0) {
            solrServer.addBeans(newsList);
            solrServer.commit();
        }

    }
}
