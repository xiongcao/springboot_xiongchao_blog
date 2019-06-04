package com.xiongchao.blog.controller;

import com.xiongchao.blog.bean.BaseResult;
import com.xiongchao.blog.util.SmsVerifyCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author William Guo
 */
@Api(description = "验证码", tags = "verifyCode")
@RestController
@RequestMapping("verifyCode")
@Validated
public class VerifyCodeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmsVerifyCodeUtil smsVerifyCodeUtil;

    @PostMapping()
    @ApiOperation("发送验证码")
    public BaseResult sendSmsVerifyCode(@RequestParam("phoneNumber") String phoneNumber) {
        String result = smsVerifyCodeUtil.sendVerifyCode(phoneNumber);
        logger.info("向手机号为{}的手机发送验证码{}", phoneNumber, result);
        return BaseResult.success();
    }
}
