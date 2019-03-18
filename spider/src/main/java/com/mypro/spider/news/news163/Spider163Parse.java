package com.mypro.spider.news.news163;

import com.google.gson.Gson;
import com.mypro.spider.domain.News;
import com.mypro.spider.utils.Constance;
import com.mypro.spider.utils.HttpClientUtils;
import com.mypro.spider.utils.JedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spider163Parse {


    public static void main(String[] args) throws IOException {
        while (true) {
            // 队列取出数据
            Jedis jedis = JedisUtils.getJedis();
            List<String> list = jedis.brpop(5, Constance.SPIDER_URL_QUEUE);
            jedis.close();
            if (list == null || list.size() == 0) {
                break;
            }
            // 解析成对象
            News news = parseNewsIntem(list.get(1));
            Gson gson = new Gson();
            // 存入持久化对象队列
            jedis = JedisUtils.getJedis();
            jedis.lpush(Constance.SPIDER_NEWS_QUEUE, gson.toJson(news));
            jedis.close();
        }
    }

    private static News parseNewsIntem(String docurl) throws IOException {
        News news = new News();
        news.setDocurl(docurl);
        // 1 获取数据
        String html = HttpClientUtils.doGet(docurl);
        // 2 解析数据
        Document document = Jsoup.parse(html);
        // 2.1 解析title
        Elements title = document.select("#epContentLeft > h1");
        news.setTitle(title.text());
        // 2.2 解析新闻时间
        Elements timeAndSource = document.select("#epContentLeft > div.post_time_source");
        String[] split = timeAndSource.text().split("　来源: ");
        news.setTime(split[0]);
        // 2.3 解析来源
        news.setSource(split[1]);
        // 2.4 解析正文
        Elements contentNode = document.select("#endText > p");
        String content = contentNode.text();
        news.setContent(content);
        // 2.5 解析编辑
        Elements editorE = document.select("#endText > div.ep-source.cDGray > span.ep-editor");
        String origionEditor = editorE.text();
        String editor = origionEditor.substring(origionEditor.indexOf("：") + 1, origionEditor.lastIndexOf("_"));
        news.setEditor(editor);
        return news;
    }
}
