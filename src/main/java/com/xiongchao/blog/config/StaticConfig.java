package com.xiongchao.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class StaticConfig extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
        registry.addResourceHandler("/**").
                addResourceLocations("file:" + path);
        super.addResourceHandlers(registry);
    }
}
