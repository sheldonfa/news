package com.mypro.news.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.mypro.news.pojo.News;
import com.mypro.news.pojo.PageBean;
import com.mypro.news.pojo.SearchVo;
import com.mypro.news.service.IndexSearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = IndexSearchService.class)
public class SolrIndexSearchServiceImpl implements IndexSearchService {

    @Autowired
    private SolrServer solrServer;

    @Override
    public List<News> findByKeywords(String keywords) throws SolrServerException {
        SolrQuery query = new SolrQuery("text:" + keywords);
        QueryResponse response = solrServer.query(query);
        return response.getBeans(News.class);
    }

    @Override
    public PageBean page(SearchVo searchVo) throws Exception {
        //1.1 基本查询
        SolrQuery query = new SolrQuery("text:" + searchVo.getKeywords());

        //1.2 高亮展示数据的设置
        query.setHighlight(true); //打开了高亮

        query.addHighlightField("title");
        query.addHighlightField("content");

        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");

        // 1.3 editor
        if (!StringUtils.isBlank(searchVo.getEditor())) {
            query.addFilterQuery("editory:" + searchVo.getEditor());
        }
        // 1.4 source
        if (!StringUtils.isBlank(searchVo.getSource())) {
            query.addFilterQuery("source:" + searchVo.getSource());
        }
        // 1.5 开始时间 结束时间
        if (!StringUtils.isBlank(searchVo.getStartDate()) || !StringUtils.isBlank(searchVo.getEndDate())) {
            if (!StringUtils.isBlank(searchVo.getStartDate()) && !StringUtils.isBlank(searchVo.getEndDate())) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                Date start = format2.parse(searchVo.getStartDate());
                Date end = format2.parse(searchVo.getEndDate());
                String startDate = format1.format(start);
                String endDate = format1.format(end);
                query.addFilterQuery("time:[" + startDate + " TO " + endDate + " ]");
            } else {
                if (!StringUtils.isBlank(searchVo.getStartDate())) {
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                    Date start = format2.parse(searchVo.getStartDate());
                    String startDate = format1.format(start);
                    query.addFilterQuery("time:[" + startDate + " TO " + Long.MAX_VALUE + " ]");
                } else {
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                    Date end = format2.parse(searchVo.getEndDate());
                    String endDate = format1.format(end);
                    query.addFilterQuery("time:[" + Long.MIN_VALUE + " TO " + endDate + " ]");
                }
            }
        }
        //1.6. 日期进行排序: 倒序
        query.setSort("time", SolrQuery.ORDER.desc);

        //1.7 封装分页参数: start  rows

        query.setStart((searchVo.getPageBean().getPage() - 1) * searchVo.getPageBean().getPageSize());
        query.setRows(searchVo.getPageBean().getPageSize());

        QueryResponse response = solrServer.query(query);
        //获取高亮展示内容
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        SolrDocumentList documentList = response.getResults();
        List<News> newsList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SolrDocument document : documentList) {
            News news = new News();
            String id = (String) document.get("id");
            news.setId(Long.parseLong(id));

            String title = (String) document.get("title");
            //获取title的高亮内容
            Map<String, List<String>> listMap = map.get(id);
            List<String> list = listMap.get("title");
            if (list != null && list.size() > 0) {
                // 说明title是有高亮的
                title = list.get(0);
            }

            news.setTitle(title);

            Date time = (Date) document.get("time"); // 在solr中存储的是Date类型
            //getTime: 获取某一个时间的毫秒值
            time.setTime(time.getTime() - (1000 * 60 * 60 * 8));

            news.setTime(format.format(time));

            String source = (String) document.get("source");
            news.setSource(source);

            String content = (String) document.get("content");

            //获取content的高亮内容
            list = listMap.get("content");
            if (list != null && list.size() > 0) {
                // 说明content是有高亮的
                content = list.get(0);
            }


            news.setContent(content);

            String editor = (String) document.get("editor");
            news.setEditor(editor);

            String docurl = (String) document.get("docurl");
            news.setDocurl(docurl);

            newsList.add(news);
        }

        //4. 封装pageBean: newsList,pageCount,pageNumber
        PageBean pageBean = searchVo.getPageBean();
        //4.1 封装了当前页的数据
        pageBean.setNewsList(newsList);

        //4.2 封装总条数
        Long pageCount = documentList.getNumFound();//总条数
        pageBean.setTotalCount(pageCount.intValue());

        return pageBean;
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getTime());
    }
}
