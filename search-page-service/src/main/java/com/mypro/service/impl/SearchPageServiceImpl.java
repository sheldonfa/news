package com.mypro.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.mypro.SearchPageService;
import com.mypro.news.pojo.PageBean;
import com.mypro.news.pojo.SearchVo;
import com.mypro.news.service.IndexSearchService;
import com.mypro.util.JedisUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class SearchPageServiceImpl implements SearchPageService {

    private static final String CACHE_PAGE = "cache_page";

    /**
     * 导入索引搜索服务
     */
    @Reference(timeout = 5000)
    private IndexSearchService indexSearchService;

    /**
     * 根据对应的关键词生产缓存数据
     *
     * @param keyword ： 巩俐
     * @return
     */
    @Override
    public boolean genSearchHtml(String keyword) {
        try {
            //1. 读取关键词
            //2. 根据关键词，调用索引搜索服务，查询新闻数据
            SearchVo vo = new SearchVo();
            PageBean pb = new PageBean();
            vo.setPageBean(pb);
            vo.setKeywords(keyword);
            //根据关键词，查询总共多少页数据
            PageBean pageBean = indexSearchService.page(vo);

            //默认取前5页结果数据
            int pageCount = 5;
            if (pageBean.getTotalPage() < 5) {
                pageCount = pageBean.getTotalPage();
            }

            //3. 将前5页新闻数据写入redis缓存
            for (int i = 1; i <= pageCount; i++) {
                //获取第i页的数据
                vo.getPageBean().setPage(i);
                PageBean page = indexSearchService.page(vo);
                vo.setPageBean(page);

                //写入redis缓存
                Jedis jedis = JedisUtils.getJedis();
                jedis.set(CACHE_PAGE + ":" + keyword + ":" + i, JSON.toJSONString(vo));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
