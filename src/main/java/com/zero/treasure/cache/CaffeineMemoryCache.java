package com.zero.treasure.cache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;

import java.util.concurrent.TimeUnit;

public class CaffeineMemoryCache {

    public static void main(String[] args) throws InterruptedException {
        Cache<String, String> cache = CaffeineCacheBuilder
                .createCaffeineCacheBuilder()
                .limit(10000)
                // 默认写入后5秒过期
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .buildCache();

        cache.put("key", "value", 1, TimeUnit.SECONDS);
        System.out.println(cache.get("key"));
        Thread.sleep(2 * 1000);
        System.out.println(cache.get("key"));
    }
}
