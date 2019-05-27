package com.xiongchao.blog.service;

import com.xiongchao.blog.bean.AppConfig;
import com.xiongchao.blog.dao.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppConfigService {

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Caching(evict = {
            @CacheEvict(value = "findAppConfigByAppId", key = "#appConfig.appId"),
            @CacheEvict(value = "findAppConfigByAdminId", key = "#appConfig.adminId")
    })
    public AppConfig save(AppConfig appConfig) {
        return appConfigRepository.save(appConfig);
    }

    public Optional<AppConfig> findById(Integer id) {
        return appConfigRepository.findById(id);
    }

    @Cacheable(value = "findAppConfigByAppId", key = "#appId")
    public AppConfig findByAppId(String appId) {
        return appConfigRepository.findByAppId(appId);
    }

    public List<AppConfig> findAll() {
        return appConfigRepository.findAll();
    }

    @Cacheable(value = "findAppConfigByAdminId", key = "#adminId")
    public AppConfig findByAdminId(Integer adminId) {
        return appConfigRepository.findByAdminId(adminId);
    }

    @Caching(evict = {
            @CacheEvict(value = "findAppConfigByAppId", allEntries = true),
            @CacheEvict(value = "findAppConfigByAdminId", allEntries = true)
    })
    public void deleteById(Integer id) {
        appConfigRepository.deleteById(id);
    }
}
