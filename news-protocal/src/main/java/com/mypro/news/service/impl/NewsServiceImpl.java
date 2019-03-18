package com.mypro.news.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypro.news.mapper.NewsMapper;
import com.mypro.news.pojo.News;
import com.mypro.news.pojo.PageBean;
import com.mypro.news.pojo.SearchVo;
import com.mypro.news.service.IndexSearchService;
import com.mypro.news.service.IndexWriterService;
import com.mypro.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private IndexWriterService indexWriterService;

    @Reference
    private IndexSearchService indexSearchService;

    @Override
    public void indexWriter() throws Exception {
        //1. 从redis中获取上一次的最大值id
        Jedis jedis = jedisPool.getResource();
        String nextMaxIdStr = jedis.get("bigData:news:nextMaxId");
        jedis.close();
        if (nextMaxIdStr == null || "".equals(nextMaxIdStr)) {
            nextMaxIdStr = "0";
        }
        int nextMaxId = Integer.parseInt(nextMaxIdStr);
        while (true) {

            List<News> newsList = newsMapper.findByNextMaxId(nextMaxId);
            if (newsList == null || newsList.size() == 0) {
                //无法获取到数据
                jedis = jedisPool.getResource();
                jedis.set("bigData:news:nextMaxId", nextMaxId + "");
                jedis.close();
                break;
            }

            //2. 执行相关的操作:日期的转换
            //2018-12-06 14:41:42 :
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            for (News news : newsList) {
                String oldTime = news.getTime();

                Date oldDate = format1.parse(oldTime);

                String newTime = format2.format(oldDate);
                news.setTime(newTime);
            }

            //3. 调用solr服务写入索引

            indexWriterService.saveNews(newsList);

            //4. 获取最大值
            nextMaxId = newsMapper.getNextMaxId(nextMaxId);
        }
    }

    @Override
    public PageBean indexSearch(SearchVo searchVo) throws Exception {
        String str = "<font";
        String str2 = "</font>";

        PageBean pageBean = indexSearchService.page(searchVo);
        for (News news : pageBean.getNewsList()) {
            String content = news.getContent();
            if (content.length() > 70) {
//                Pattern p = Pattern.compile(content);
//                String[] split1 = p.split("<font color=red>");
//                for(String c:  split1){
//                    Pattern p2 = Pattern.compile(c);
//                    String[] newsplit = p2.split("</font>");
//
//                }
                content = content.substring(0, 69);

                content += "...";
                news.setContent(content);
            }
        }
        return pageBean;
    }

    public static void main(String[] args) {
        String s = "asgodssgoodsssagodssgood";
        Pattern compile2 = Pattern.compile(s);
        String[] m2 = s.split("god.*?(good)");
        for(String s1: m2){
            System.out.println(s1);
        }
    }
}
