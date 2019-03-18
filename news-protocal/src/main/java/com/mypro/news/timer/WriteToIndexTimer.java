package com.mypro.news.timer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypro.news.service.IndexWriterService;
import com.mypro.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
public class WriteToIndexTimer {

    @Autowired
    private NewsService newsService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void execute(){
        try {
            newsService.indexWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
