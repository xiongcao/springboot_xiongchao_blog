package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.Constants;
import com.xiongchao.blog.bean.User;
import com.xiongchao.blog.service.UserService;
import com.xiongchao.blog.util.IPUtil;
import com.xiongchao.blog.util.SmsVerifyCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(description = "后台用户管理")
@RestController
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private UserService userService;

    @GetMapping("findUser")
    @ApiOperation("根据用户名称查询用户信息")
    public BaseResult findUserByUserName(@RequestParam(value = "name") String name) {
        User user = userService.findByUsername(name);
        if (user == null) {
            return BaseResult.failure("用户不存在");
        }
        return BaseResult.success(user);
    }

    @PostMapping("signIn")
    @ApiOperation("管理员账号密码登录swagger")
    public BaseResult signIn(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        return BaseResult.success();
    }

    @GetMapping("signInSuccess")
    @ApiOperation("根据用户名称查询用户信息")
    public BaseResult findUserByUserName(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        request.getSession().setAttribute(Constants.ADMIN_ID, user.getId());
        request.getSession().setAttribute(Constants.ADMIN_NAME, user.getName());
        user.setPasswordAttemptCount(0);
        user.setLockedDate(null);
        userService.save(user);
        logger.info("管理员:{} 登录成功 登录IP:{}", username, IPUtil.getClientIP(request));
        return BaseResult.success(user);
    }

    @GetMapping("loginIn")
    @ApiOperation("管理员账号密码登录")
    public BaseResult signIn(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpServletRequest request) {
        User user = userService.findByUsername(username);

        //用户名
        if (null == user) {
            logger.warn("管理员:{} 登录失败:用户名错误 登录IP:{}", username, IPUtil.getClientIP(request));
            return BaseResult.failure("用户名错误");
        }

        // 锁定状态
        if (User.LOCKED_BY_PASSWORD == user.getStatus()) {
            if (new Date().after(DateUtils.addHours(user.getLockedDate(), 24))) {//超过24小时自动解锁
                user.setStatus(User.NORMAL_STATUS);
                user.setPasswordAttemptCount(0);
                user.setLockedDate(null);
                userService.save(user);
            }else {
                logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多 登录IP:{}", username, IPUtil.getClientIP(request));
                return BaseResult.failure("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
            }
        }
        if (User.LOCKED_BY_LEADER == user.getStatus()) {
            logger.warn("管理员:{} 登录 登录失败:用户已被上级锁定 登录IP:{}", username, IPUtil.getClientIP(request));
            return BaseResult.failure("用户已被上级锁定");
        }

        // 密码对比
        if (!user.passwordMatches(password)) {
            user.setPasswordAttemptCount(user.getPasswordAttemptCount() + 1);
            if(user.getPasswordAttemptCount() > User.PASSWORD_ATTEMPT_MAX_COUNT) {//密码尝试超过上限
                user.setStatus(User.LOCKED_BY_PASSWORD);
                user.setLockedDate(new Date());
                userService.save(user);
                logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多 登录IP:{}", username, IPUtil.getClientIP(request));
                return BaseResult.failure("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
            }

            userService.save(user);
            logger.warn("管理员:{} 登录失败:密码错误 登录IP:{}", username, IPUtil.getClientIP(request));
            return BaseResult.failure("密码错误");
        }
        request.getSession().setAttribute(Constants.ADMIN_ID, user.getId());
        request.getSession().setAttribute(Constants.ADMIN_NAME, user.getName());

        user.setPasswordAttemptCount(0);
        user.setLockedDate(null);
        userService.save(user);
        logger.info("管理员:{} 登录成功 登录IP:{}", username, IPUtil.getClientIP(request));
        return BaseResult.success(user);
    }


    @GetMapping("signInByPhone")
    @ApiOperation("管理员电话号码/验证码登录")
    public BaseResult signInByPhone(@RequestParam("phoneNumber") String phoneNumber,
                                    @RequestParam("code") String code,
                                    HttpServletRequest request) {
        User user = userService.findByPhoneNumber(phoneNumber);

        //用户名
        if (null == user) {
            logger.warn("管理员:{} 登录失败:电话号码尚未注册 登录IP:{}", phoneNumber, IPUtil.getClientIP(request));
            return BaseResult.failure("电话号码尚未注册");
        }

        // 锁定状态
        if (User.LOCKED_BY_PASSWORD.equals(user.getStatus())) {
            logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多 登录IP:{}", user.getName(), IPUtil.getClientIP(request));
            return BaseResult.failure("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
        }
        if (User.LOCKED_BY_LEADER.equals(user.getStatus())) {
            logger.warn("管理员:{} 登录 登录失败:用户已被上级锁定 登录IP:{}", user.getName(), IPUtil.getClientIP(request));
            return BaseResult.failure("用户已被上级锁定");
        }

        // 校验验证码是否正确
        SmsVerifyCodeUtil.checkVerifyCode(phoneNumber, code);

        request.getSession().setAttribute(Constants.ADMIN_ID, user.getId());
        request.getSession().setAttribute(Constants.ADMIN_NAME, user.getName());
        logger.info("管理员:{} 登录成功 登录IP:{}", user.getName(), IPUtil.getClientIP(request));
        return BaseResult.success(user);
    }

}
