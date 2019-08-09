package com.abreaking.blog.utils;

import org.junit.Test;

/**
 * @{USER}
 * @{DATE}
 */
public class MapCacheTest {

    @Test
    public void test01(){
        MapCache cache = MapCache.single();
        cache.hset("zhangsan","name","zs");
        cache.hset("zhangsan","age","11");
        Object hget = cache.hget("zhangsan", "name");
        System.out.println(hget);
    }
}
