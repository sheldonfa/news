package com.mypro.news.controller;

import com.alibaba.fastjson.JSON;
import com.mypro.news.pojo.PageBean;
import com.mypro.news.pojo.SearchVo;
import com.mypro.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageBean search(SearchVo search) {
        System.out.println(search);
        PageBean pageBean = null;
        try {
            pageBean = newsService.indexSearch(search);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageBean;
    }

}
