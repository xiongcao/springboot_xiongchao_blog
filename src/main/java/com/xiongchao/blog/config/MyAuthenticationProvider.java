package com.xiongchao.blog.config;

import com.xiongchao.blog.bean.User;
import com.xiongchao.blog.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerAdmin = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户登录时输入的用户名
        String username = authentication.getName();
        // 根据用户名查询系统中的用户信息
        User user = userService.findByUsername(username);
        // 如果用户 null，说明查找用户功能出现异常，抛出 AuthenticationServiceException
        if (null == user) {
            logger.warn("管理员:{} 登录失败:用户名错误", username);
            throw new RuntimeException("用户名错误");
        }

        // 锁定状态
        if (User.LOCKED_BY_PASSWORD.equals(user.getStatus())) {
            if (new Date().after(DateUtils.addHours(user.getLockedDate(), 24))) {//超过24小时自动解锁
                user.setStatus(User.NORMAL_STATUS);
                user.setPasswordAttemptCount(0);
                user.setLockedDate(null);
                userService.save(user);
            } else {
                logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多 ", username);
                throw new RuntimeException("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
            }
        }
        if (User.LOCKED_BY_LEADER.equals(user.getStatus())) {
            logger.warn("管理员:{} 登录 登录失败:用户已被上级锁定", username);
            throw new RuntimeException("用户已被上级锁定");
        }

        // 密码对比
        String password = (String) authentication.getCredentials();
        if (!user.passwordMatches(password)) {
            user.setPasswordAttemptCount(user.getPasswordAttemptCount() + 1);
            if (user.getPasswordAttemptCount() > User.PASSWORD_ATTEMPT_MAX_COUNT) {//密码尝试超过上限
                user.setStatus(User.LOCKED_BY_PASSWORD);
                user.setLockedDate(new Date());
                userService.save(user);
                logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多", username);
                throw new RuntimeException("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
            }

            userService.save(user);
            logger.warn("管理员:{} 登录失败:密码错误", username);
            throw new RuntimeException("密码错误");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new UsernamePasswordAuthenticationToken(authentication, authentication.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
