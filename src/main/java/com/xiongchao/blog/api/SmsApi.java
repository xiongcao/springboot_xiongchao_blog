package com.xiongchao.blog.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author William Guo
 */
@Service
@FeignClient(name = "sms", url = "http://www.wemediacn.net/webservice/smsservice.asmx")
public interface SmsApi {

    String FORMAT_ID     = "40";
    String SCHEDULE_DATE = "2010-1-1";
    String TOKEN_ID      = "7100114730321073";

    String TPL_REGISTER = "尊敬的顾客，您的会员注册验证码是：%s。工作人员不会向您索取该密码，请切勿告知他人或在其他平台使用！";

    /**
     * 发送短信
     *
     * @param mobile       手机号
     * @param formatID
     * @param content      内容
     * @param scheduleDate
     * @param tokenID
     * @return
     */
    @GetMapping("SendSMS")
    String sendSMS(@RequestParam("mobile") String mobile,
                   @RequestParam("FormatID") String formatID,
                   @RequestParam("Content") String content,
                   @RequestParam("ScheduleDate") String scheduleDate,
                   @RequestParam("TokenID") String tokenID);

    /**
     * 发送短信验证码
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    default String sendVerifyCode(String phoneNumber, String code) {
        return sendSMS(phoneNumber, FORMAT_ID, String.format(TPL_REGISTER, code), SCHEDULE_DATE, TOKEN_ID);
    }
}
