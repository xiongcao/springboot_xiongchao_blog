package com.xiongchao.blog.dao;

import com.xiongchao.blog.bean.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {

    AppConfig findByAppId(String appId);

    AppConfig findByAdminId(Integer adminId);
}
