package com.xiongchao.blog.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by cachee on 2/22/2018.
 */
public class MyConcurrentMapCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);

    private boolean dynamic = true;

    private boolean allowNullValues = true;

    private long expireTime = -1;

    private long maximumSize = 100;

    /**
     * 缓存参数的分隔符
     * 数组元素0=缓存的名称
     * 数组元素1=缓存过期时间TTL
     * 数组元素2=缓存在多少秒开始主动失效来强制刷新
     */
    private static final String SEPARATOR = "#";

    /**
     * Construct a dynamic ConcurrentMapCacheManager,
     * lazily creating cache instances as they are being requested.
     */
    public MyConcurrentMapCacheManager() {
    }

    /**
     * Construct a static ConcurrentMapCacheManager,
     * managing caches for the specified cache names only.
     */
    public MyConcurrentMapCacheManager(long expireTime, long maximumSize) {
        if(expireTime > 0)
            this.expireTime = expireTime;
        if(maximumSize > 0)
            this.maximumSize = maximumSize;
    }


    /**
     * Specify the set of cache names for this CacheManager's 'static' mode.
     * <p>The number of caches and their names will be fixed after a call to this method,
     * with no creation of further cache regions at runtime.
     * <p>Calling this with a {@code null} collection argument resets the
     * mode to 'dynamic', allowing for further creation of caches again.
     */
    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createConcurrentMapCache(name));
            }
            this.dynamic = false;
        }
        else {
            this.dynamic = true;
        }
    }

    /**
     * Specify whether to accept and convert {@code null} values for all caches
     * in this cache manager.
     * <p>Default is "true", despite ConcurrentHashMap itself not supporting {@code null}
     * values. An internal holder object will be used to store user-level {@code null}s.
     * <p>Note: A change of the null-value setting will reset all existing caches,
     * if any, to reconfigure them with the new null-value requirement.
     */
    public void setAllowNullValues(boolean allowNullValues) {
        if (allowNullValues != this.allowNullValues) {
            this.allowNullValues = allowNullValues;
            // Need to recreate all Cache instances with the new null-value configuration
            for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
                entry.setValue(createConcurrentMapCache(entry.getKey()));
            }
        }
    }

    /**
     * Return whether this cache manager accepts and converts {@code null} values
     * for all of its caches.
     */
    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }


    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override
    public Cache getCache(String name) {
        String[] cacheParams = name.split(SEPARATOR);
        String cacheName = cacheParams[0];

        if (StringUtils.isEmpty(cacheName)) {
            return null;
        }

        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = getExpirationSecondTime(cacheName, cacheParams);
        // 自动刷新时间，默认是0
        Long preloadSecondTime = getExpirationSecondTime(cacheParams);

        Cache cache = this.cacheMap.get(cacheName);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(cacheName);
                if (cache == null) {
                    cache = createConcurrentMapCache(cacheName, expirationSecondTime);
                    this.cacheMap.put(cacheName, cache);
                }
            }
        }
        return cache;
    }

    /**
     * Create a new ConcurrentMapCache instance for the specified cache name.
     * @param name the name of the cache
     * @return the ConcurrentMapCache (or a decorator thereof)
     */
    protected Cache createConcurrentMapCache(String name, Long expirationSecondTime) {
        //return new ConcurrentMapCache(name, isAllowNullValues());
        //此处改用GOOGLE GUAVA的构造MANAGER方式
        if(expirationSecondTime < 0) {
            return createConcurrentMapCache(name);
        }
        return new ConcurrentMapCache(name,
                CacheBuilder.newBuilder()
                        .expireAfterWrite(expirationSecondTime, TimeUnit.SECONDS)
                        .maximumSize(this.maximumSize)
                        .build()
                        .asMap(),
                isAllowNullValues());
    }

    protected Cache createConcurrentMapCache(String name) {
        //return new ConcurrentMapCache(name, isAllowNullValues());
        //此处改用GOOGLE GUAVA的构造MANAGER方式
        return new ConcurrentMapCache(name,
                CacheBuilder.newBuilder()
                        .maximumSize(this.maximumSize)
                        .build()
                        .asMap(),
                isAllowNullValues());
    }

    /**
     * 获取过期时间
     *
     * @return
     */
    private long getExpirationSecondTime(String cacheName, String[] cacheParams) {
        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = this.expireTime;

        // 设置key有效时间
        if (cacheParams.length > 1) {
            String expirationStr = cacheParams[1];
            if (!StringUtils.isEmpty(expirationStr)) {
                // 支持配置过期时间使用EL表达式读取配置文件时间
                expirationSecondTime = Long.parseLong(expirationStr);
            }
        }

        return expirationSecondTime;
    }

    /**
     * 获取自动刷新时间
     *
     * @return
     */
    private long getExpirationSecondTime(String[] cacheParams) {
        // 自动刷新时间，默认是0
        Long preloadSecondTime = 0L;
        // 设置自动刷新时间
        if (cacheParams.length > 2) {
            String preloadStr = cacheParams[2];
            if (!StringUtils.isEmpty(preloadStr)) {
                // 支持配置刷新时间使用EL表达式读取配置文件时间
                preloadSecondTime = Long.parseLong(preloadStr);
            }
        }
        return preloadSecondTime;
    }

}
