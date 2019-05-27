package com.xiongchao.blog.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gavin
 * @date 2/22/2018.
 */
@Configuration
public class MyCache {

    @Bean("cacheManager")
    public CacheManager myCache() {
        return new MyConcurrentMapCacheManager();
    }
}
