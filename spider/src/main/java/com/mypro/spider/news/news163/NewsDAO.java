package com.mypro.spider.news.news163;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mypro.spider.domain.News;
import com.mypro.spider.producer.SpiderKafkaProducer;
import com.mypro.spider.utils.Constance;
import com.mypro.spider.utils.JedisUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;

import java.beans.PropertyVetoException;
import java.util.List;

public class NewsDAO {

    private static JdbcTemplate template = new JdbcTemplate();

    static {
        ComboPooledDataSource datasource = new ComboPooledDataSource();
        try {
            datasource.setDriverClass("com.mysql.jdbc.Driver");
            datasource.setUser("root");
            datasource.setPassword("root");
            datasource.setJdbcUrl("jdbc:mysql://node02:3306/bd_news?characterEncoding=utf-8");
            template.setDataSource(datasource);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        while (true) {
            // 队列取出对象
            Jedis jedis = JedisUtils.getJedis();
            List<String> newsJsonList = jedis.brpop(5, Constance.SPIDER_NEWS_QUEUE);
            jedis.close();
            if (newsJsonList == null || newsJsonList.size() == 0) {
                break;
            }
            // 反序列化
            Gson gson = new Gson();
            News news = gson.fromJson(newsJsonList.get(1), News.class);
            // 再次判断重复性
            jedis = JedisUtils.getJedis();
            if (jedis.sismember(Constance.SPIDER_URL_INSERTEDSET, news.getDocurl())) {
                jedis.close();
                continue;
            }
            // 持久化
            template.update("INSERT INTO `bd_news`.`news` " +
                            "(`id`, `title`, `time`, `source`, `content`, `editor`, `docurl`) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);", null,
                    news.getTitle(), news.getTime(), news.getSource(),
                    news.getContent(), news.getEditor(), news.getDocurl());
            // 缓存连接
            jedis.sadd(Constance.SPIDER_URL_INSERTEDSET, news.getDocurl());
            jedis.close();
        }
    }
}
