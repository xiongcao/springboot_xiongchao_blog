package com.xiongchao.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiongchao.blog.bean.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Gavin
 * @date 4/27/2018.
 */
@Api(description = "首页")
@Controller
@RequestMapping()
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger("FRONTEND");

    @ApiOperation("获取服务器时间")
    @GetMapping("time")
    @ResponseBody
    public BaseResult time() {
        return BaseResult.success(System.currentTimeMillis());
    }

    @ApiOperation("前端保存日志")
    @PostMapping("log")
    @ResponseBody
    public void frontendLog(@RequestBody String log) {
        JSONObject jsonObject = JSON.parseObject(log);
        logger.info(jsonObject.getString("log"));
    }

    @GetMapping("swagger")
    public String swagger(HttpSession session) {
//        Admin user = userService.findByPhoneNumber("15727055403");
//        Admin user = userService.findByPhoneNumber("18040540452");
//        session.setAttribute(Constants.ADMIN_ID, user.getId());
//        session.setAttribute(Constants.ADMIN_ID, user.getId());
//        session.setAttribute(Constants.USER, user);
//        session.setAttribute(Constants.ADMIN_ID, 1);  //  开启我
//        session.setAttribute(Constants.ADMIN_NAME, "super");  //  开启我
//        session.setAttribute(Constants.ADMIN_ID, 1);
//        session.setAttribute(Constants.ADMIN_NAME, "super");
//        session.setAttribute(Constants.ADMIN_ID, 5);
//        session.setAttribute(Constants.ADMIN_NAME, "xiongchao");
//        session.setAttribute(Constants.USER_ID, 2); //  开启我
//        session.setAttribute(Constants.WX_MP_USER, new WxMpUser());
        return "redirect:/swagger-ui.html";
    }
}
