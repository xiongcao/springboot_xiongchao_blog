package com.xiongchao.blog.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Bean
    public IAcsClient acsClient() {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIavXhMTaMcfDD", "F2Tq9TPyPDtc7X3Kh4c3ZUI6fToQHH");
        return new DefaultAcsClient(profile);
    }
}
