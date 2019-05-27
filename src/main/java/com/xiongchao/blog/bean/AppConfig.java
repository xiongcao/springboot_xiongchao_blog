package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "小程序配置参数")
@Entity
@DynamicUpdate
@DynamicInsert
public class AppConfig extends BaseEntity {

    @ApiModelProperty("后台用户id")
    @Column(length = 11, unique = true)
    @NotNull(message = "后台用户id不能为空")
    private Integer adminId;

    @ApiModelProperty("app id")
    @Column(length = 100, unique = true)
    @NotNull(message = "app id不能为空")
    private String appId;

    @ApiModelProperty("app 秘钥")
    @NotNull(message = "app 秘钥不能为空")
    private String secret;

    @ApiModelProperty("app 信息格式")
    @Column(columnDefinition = "varchar(255) default 'JSON'")
    private String msgDataFormat;

    @ApiModelProperty("备注")
    @Column(columnDefinition="TEXT")
    private String remark;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMsgDataFormat() {
        return msgDataFormat;
    }

    public void setMsgDataFormat(String msgDataFormat) {
        this.msgDataFormat = msgDataFormat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
