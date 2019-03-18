package com.mypro.news.mapper;

import com.mypro.news.pojo.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper Interface:News
 *
 * @author fangxin
 * @date 2019-3-11
 */

public interface NewsMapper {
    /**
     * 添加
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Integer insert(News news);

    /**
     * 选择性添加
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Integer insertSelective(News news);

    /**
     * 根据主键删除
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Integer deleteById(@Param("id") Long id);

    /**
     * 根据主键数组删除
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Integer deleteByIds(@Param("ids") Long[] ids);

    /**
     * 条件删除
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Integer delete(News news);

    /**
     * 更新
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Integer update(News news);

    /**
     * 查询
     *
     * @author fangxin
     * @date 2019-3-11
     */
    List<News> find(News news);

    /**
     * 查询全部
     *
     * @author fangxin
     * @date 2019-3-11
     */
    List<News> findAll();

    /**
     * 查询数量
     *
     * @author fangxin
     * @date 2019-3-11
     */
    Long findCount(News news);

    /**
     * 根据主键查询
     *
     * @author fangxin
     * @date 2019-3-11
     */
    News findById(Long id);

    //1. 获取数据
    List<News> findByNextMaxId(Integer nextMaxId);

    //2. 获取最大值
    Integer getNextMaxId(Integer nextMaxId);
}