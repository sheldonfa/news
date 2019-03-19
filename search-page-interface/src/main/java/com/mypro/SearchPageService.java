package com.mypro;

/**
 * @author itheima
 * @Title: SearchPageService
 * @ProjectName gossip-parent
 * @Description: TODO
 * @date 2018/12/1914:55
 */
public interface SearchPageService {

    /**
     * 根据对应的关键词生产缓存数据
     * @param keyword
     * @return
     */
    public boolean genSearchHtml(String keyword);
}
