package com.mypro.spider.news.news163;

import com.google.gson.Gson;
import com.mypro.spider.domain.News;
import com.mypro.spider.utils.Constance;
import com.mypro.spider.utils.HtmlUtils;
import com.mypro.spider.utils.HttpClientUtils;
import com.mypro.spider.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spider163Step1 {

    public static void main(String[] args) throws IOException {
        // 发送请求，获取响应
        String nextUrl = "http://ent.163.com/special/000380VU/newsdata_index.js?callback=data_callback";
        String page = "02";
        while (true) {
            String origionData = HttpClientUtils.doGet(nextUrl,"gbk");
            if (origionData == null) {
                break;
            }
            String json = HtmlUtils.parseOrigionToJson(origionData);
            List<News> newsList = new ArrayList<>();
            Gson gson = new Gson();
            List<Map<String, String>> list = gson.fromJson(json, List.class);
            for (Map<String, String> m : list) {
                // docurl
                String docurl = m.get("docurl");
                if (docurl.contains("photoview")) {
                    continue;
                }
                // 查重
                Jedis jedis = JedisUtils.getJedis();
                Boolean flag = jedis.sismember(Constance.SPIDER_URL_INSERTEDSET, docurl);
                jedis.close();
                // 如果重复跳过
                if (flag) {
                    continue;
                }
                // 添加到redis到队列
                jedis = JedisUtils.getJedis();
                jedis.lpush(Constance.SPIDER_URL_QUEUE, docurl);
                jedis.close();
            }
            nextUrl = "http://ent.163.com/special/000380VU/newsdata_index_" + page + ".js?callback=data_callback";
            int i = Integer.parseInt(page);
            i++;
            if (i < 10) {
                page = "0" + i;
            } else {
                page = i + "";
            }
        }
    }
}
