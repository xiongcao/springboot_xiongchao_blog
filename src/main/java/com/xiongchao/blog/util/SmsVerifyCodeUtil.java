package com.xiongchao.blog.util;

import com.xiongchao.blog.api.SmsApi;
import com.xiongchao.blog.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Guo on 2017/5/23.
 */
@Component
@EnableScheduling
public class SmsVerifyCodeUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SmsApi smsApi;

    @Autowired
    private SmsService smsService;

    private static long VALIDITY_PERIOD = 1000 * 60 * 30;

    private static Map<String, VerifyCode> VERIFY_CODE_MAP = new HashMap<>();

    /**
     * 自动删除暂存
     */
    @Scheduled(cron = "0 0 0 * * *")
    private void clearVerifyCodeMap() {
        VERIFY_CODE_MAP.clear();
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     * @return
     */
    public String sendVerifyCode(String phoneNumber) {

        // 获取暂存
        VerifyCode verifyCode = VERIFY_CODE_MAP.get(phoneNumber);

        if (verifyCode == null) {
            verifyCode = new VerifyCode();
            verifyCode.setSend(0);
        } else {
            if (verifyCode.getSend() > 10) {
                throw new RuntimeException("发送次数已达上限,请明天再试");
            }
            verifyCode.setSend(verifyCode.getSend() + 1);
        }

        // 生成验证码
        String code = RandomStringUtils.randomNumeric(6);

        verifyCode.setCode(code);
        // 校验次数,校验失败一次+1
        verifyCode.setCheck(0);
        // 有效期 当前时间 + 30分钟有效期
        verifyCode.setDate(new Date(System.currentTimeMillis() + VALIDITY_PERIOD));

        // 暂存
        VERIFY_CODE_MAP.put(phoneNumber, verifyCode);

        // 发送短信
        smsService.sendSmsLogin(phoneNumber, code);
        return code;
    }

    /**
     * 验证码校验
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     * @return
     */
    public static void checkVerifyCode(String phoneNumber, String code) {

        // 获取暂存
        VerifyCode verifyCode = VERIFY_CODE_MAP.get(phoneNumber);

        if (verifyCode == null) throw new RuntimeException("请先发送验证码");

        // 校验次数是否大于5次 或  发送时间是否在当前时间之后true有效false失效
        if (verifyCode.getCheck() > 5 || verifyCode.getDate().before(new Date())) {
            throw new RuntimeException("验证码已失效,请重新发送");
        }

        // 判断验证码是否正确
        if (!verifyCode.getCode().equals(code)) {
            // 校验失败次数 + 1
            verifyCode.setCheck(verifyCode.getCheck() + 1);

            VERIFY_CODE_MAP.put(phoneNumber, verifyCode);
            throw new RuntimeException("验证码有误,请重试");
        }

        // 校验成功
        VERIFY_CODE_MAP.remove(phoneNumber);
    }

    static class VerifyCode {
        /**
         * 验证码
         */
        private String  code;
        /**
         * 校验次数,校验失败次数 + 1
         */
        private Integer check;
        /**
         * 发送次数,发送一次+1
         */
        private Integer send;
        /**
         * 发送时间
         */
        private Date    date;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getCheck() {
            return check;
        }

        public void setCheck(Integer check) {
            this.check = check;
        }

        public Integer getSend() {
            return send;
        }

        public void setSend(Integer send) {
            this.send = send;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
