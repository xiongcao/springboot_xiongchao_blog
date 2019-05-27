package com.xiongchao.blog.config.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiongchao.blog.bean.AppConfig;
import com.xiongchao.blog.service.AppConfigService;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序配置
 */
@Configuration
@ConditionalOnClass(WxMaService.class)
public class WxMaConfiguration {
    private static final WxMaMessageHandler templateMsgHandler = (wxMessage, context, service, sessionManager) ->
            service.getMsgService().sendTemplateMsg(WxMaTemplateMessage.newBuilder()
                    .templateId("此处更换为自己的模板id")
                    .formId("自己替换可用的formid")
                    .data(Lists.newArrayList(
                            new WxMaTemplateMessage.Data("keyword1", "339208499", "#173177")))
                    .toUser(wxMessage.getFromUser())
                    .build());

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        System.out.println("收到消息：" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.TEXT().content("收到信息为：" + wxMessage.toJson())
                .toUser(wxMessage.getFromUser()).build());
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) ->
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.TEXT().content("回复文本消息")
                    .toUser(wxMessage.getFromUser()).build());

    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMediaUploadResult uploadResult = service.getMediaService()
                    .uploadMedia("image", "png",
                            ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .IMAGE()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .IMAGE()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    };

    private static Map<String, WxMaMessageRouter> routers = Maps.newHashMap();
    private static Map<String, WxMaService> wxMaServices = Maps.newHashMap();
    private static Map<String, Integer> adminIds = Maps.newHashMap();

    @Autowired
    private AppConfigService appConfigService;

    @PostConstruct
    public void initServices() {
        List<AppConfig> appConfigs = appConfigService.findAll();
//        if(appConfigs.isEmpty()) {

//            throw new RuntimeException("未查到小程序注册信息");
//        }
        for(AppConfig appConfig : appConfigs) {
            appInit(appConfig);
        }
    }

    public void appInit(AppConfig appConfig) {
        WxMaProperties wxMaProperties = new WxMaProperties(appConfig);

        //注册信息
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(wxMaProperties.getAppId());
        config.setSecret(wxMaProperties.getSecret());
        config.setToken(wxMaProperties.getToken());
        config.setAesKey(wxMaProperties.getAesKey());
        config.setMsgDataFormat(wxMaProperties.getMsgDataFormat());

        //服务
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        wxMaServices.put(config.getAppid(), service);

        //路由
        routers.put(config.getAppid(), this.router(service));

        //管理员Id
        adminIds.put(config.getAppid(), wxMaProperties.getAdminId());
    }

    public void deleteAppConfig(String appId) {
        wxMaServices.remove(appId);
        //路由
        routers.remove(appId);
        //管理员Id
        adminIds.remove(appId);
    }

    private WxMaMessageRouter router(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router
                .rule().handler(logHandler).next()
                .rule().async(false).content("模板").handler(templateMsgHandler).end()
                .rule().async(false).content("文本").handler(textHandler).end()
                .rule().async(false).content("图片").handler(picHandler).end()
                .rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }

    public static Map<String, WxMaMessageRouter> getRouters() {
        return routers;
    }

    public static void setRouters(Map<String, WxMaMessageRouter> routers) {
        WxMaConfiguration.routers = routers;
    }

    public static Map<String, WxMaService> getWxMaServices() {
        return wxMaServices;
    }

    public static void setWxMaServices(Map<String, WxMaService> wxMaServices) {
        WxMaConfiguration.wxMaServices = wxMaServices;
    }

    public static Map<String, Integer> getAdminIds() {
        return adminIds;
    }

    public static void setAdminIds(Map<String, Integer> adminIds) {
        WxMaConfiguration.adminIds = adminIds;
    }
}
