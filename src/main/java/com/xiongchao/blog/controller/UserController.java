package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.bean.User;
import com.xiongchao.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "后台用户管理")
@RestController
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Logger loggerAdmin = LoggerFactory.getLogger("ADMIN");

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

}
