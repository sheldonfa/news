package com.mypro.spider.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {
    private static JedisPool jedisPool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100); //最大连接数
        config.setMaxIdle(50); //最大的闲时的数量
        config.setMinIdle(20);//最小的闲时的数量
        jedisPool  = new JedisPool(config,"node01",6379);
    }


    //返回连接
    public static Jedis getJedis() {

        return jedisPool.getResource();
    }

    public static void main(String[] args) {
        String s1 = "金毛狮王是人工翻白眼！网友翻徐锦江剧照大呼敬业";
        String s2 = "金毛狮王是人工翻白眼！网友翻徐锦江剧照大呼敬业";
        boolean equals = s1.equals(s2);
        System.out.println(equals);
    }
}
