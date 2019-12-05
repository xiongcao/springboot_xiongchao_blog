package com.xiongchao.blog.controller;

import com.alibaba.fastjson.JSON;
import com.xiongchao.blog.DTO.UserDTO;
import com.xiongchao.blog.bean.*;
import com.xiongchao.blog.service.UserService;
import com.xiongchao.blog.util.IPUtil;
import com.xiongchao.blog.util.SmsVerifyCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Api(description = "用户管理")
@Controller
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerUser = LoggerFactory.getLogger("ADMIN");

    @Autowired
    private UserService userService;

    @PostMapping("signIn")
    @ResponseBody
    @ApiOperation("管理员账号密码登录swagger")
    public BaseResult signIn(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        return BaseResult.success();
    }

    @GetMapping("signInSuccess")
    @ResponseBody
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
    @ResponseBody
    @ApiOperation("管理员账号密码登录")
    public BaseResult loginIn(@RequestParam("username") String username,
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
            } else {
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
            if (user.getPasswordAttemptCount() > User.PASSWORD_ATTEMPT_MAX_COUNT) {//密码尝试超过上限
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
        request.getSession().setAttribute(Constants.ROLE, user.getRole());
        user.setPasswordAttemptCount(0);
        user.setLockedDate(null);
        userService.save(user);
        logger.info("管理员:{} 登录成功 登录IP:{}", username, IPUtil.getClientIP(request));
        return BaseResult.success(user);
    }

    @GetMapping("signInByPhone")
    @ResponseBody
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

    @GetMapping("admin/findById")
    @ResponseBody
    @ApiOperation("后台根据用户Id查询用户信息")
    public BaseResult findById(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                               @RequestParam("id") Integer id, HttpServletRequest request) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
        return BaseResult.success(user);
    }

    @GetMapping("findById")
    @ResponseBody
    @ApiOperation("前台根据用户Id查询用户信息")
    public BaseResult findById(@RequestParam("id") Integer id, HttpServletRequest request) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
        return BaseResult.success(user);
    }

    @GetMapping("findAdmin")
    @ResponseBody
    @ApiOperation("前台查询已经登录的用户信息")
    public BaseResult findAdmin(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId) {
        User user = userService.findById(adminId).orElseThrow(() -> new RuntimeException("用户不存在"));
        UserDTO userDTO = JSON.parseObject(JSON.toJSONString(user), UserDTO.class);
        Integer follow_number = userService.findFollowNumberByStatus(adminId, 1);
        Integer fans_number = userService.findFollowNumberByStatus(adminId, 2);
        userDTO.setFollow_number(follow_number);
        userDTO.setFans_number(fans_number);
        return BaseResult.success(userDTO);
    }

    @PostMapping("save")
    @ResponseBody
    @ApiOperation("保存用户信息")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @Valid @RequestBody User user) {
        User user1 = userService.findById(adminId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword(user1.getPassword());
//        user.passwordEncoder();
        userService.save(user);
        return BaseResult.success("添加成功");
    }

    @PostMapping("unlock")
    @ResponseBody
    @ApiOperation("锁定和解锁用户")
    public BaseResult save(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                           @RequestParam("id") Integer id,
                           @RequestParam(value = "status", required = false) @ApiParam("1: 正常 3：超管锁定") Integer status) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus(status);
        if (status == 1) { // 解锁
            user.setLockedDate(null);
            user.setPasswordAttemptCount(0);
        }
        userService.save(user);
        return BaseResult.success("成功");
    }

    @PostMapping("register")
    @ApiOperation("注册")
    public String register(@Valid @RequestBody User user) {
        String password = user.getPassword();
        user.passwordEncoder();
        userService.save(user);
        return "redirect:loginIn?username="+user.getName()+"&password="+password;
    }

    @GetMapping("findUserByName")
    @ResponseBody
    @ApiOperation("根据用户名查询是否已存在该用户")
    public BaseResult findUserByName(@RequestParam("name") String name) {
        User user = userService.findByUsername(name);
        if (null == user) {
            return BaseResult.failure(7, "该用户名尚未注册");
        }
        return BaseResult.success("该用户名已注册");
    }

    @GetMapping("findUserByPhone")
    @ResponseBody
    @ApiOperation("根据手机号查询是否已存在该用户")
    public BaseResult findUserByPhone(@RequestParam("phone") String phone) {
        User user = userService.findByPhoneNumber(phone);
        if (null == user) {
            return BaseResult.failure(7, "该手机号尚未注册");
        }
        return BaseResult.success("该手机号已注册");
    }

    @PostMapping("updatePassword")
    @ResponseBody
    @ApiOperation("修改密码")
    public BaseResult updatePassword(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId,
                                     @RequestParam("oldPwd") String oldPwd,
                                     @RequestParam("newPwd") String newPwd) {
        User user = userService.findById(adminId).orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!user.passwordMatches(oldPwd)) {
            return BaseResult.failure("原始密码不正确");
        }
        user.setPassword(newPwd);
        user.passwordEncoder();
        userService.save(user);
        return BaseResult.success();
    }

    @GetMapping("findAll")
    @ResponseBody
    @ApiOperation("查询所有用户，只有超管有权限")
    public BaseResult findAll(@ApiIgnore @SessionAttribute(Constants.ADMIN_ID) Integer adminId, PageWithSearch page) {
        if (adminId != 1) {
            return BaseResult.failure("没有权限");
        }
        return BaseResult.success(userService.findUserAll(page));
    }
}
