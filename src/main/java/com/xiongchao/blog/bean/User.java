package com.xiongchao.blog.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xiongchao.blog.util.PasswordUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(description = "用户")
@Entity
@DynamicUpdate
@DynamicInsert
public class User extends BaseEntity {

    //超管,仅限一位
    public static final String ROLE_SUPER = "ROLE_SUPER";

    //普通用户
    public static final String ROLE_USER = "ROLE_USER";

    //未关注公众号关
    public static final Integer WECHAT_UNFOLLOWED = 0;

    //已关注公众号关
    public static final Integer WECHAT_FOLLOWED = 1;

    //用户正常状态
    public static final Integer NORMAL_STATUS = 1;

    //密码尝试锁定锁定
    public static final Integer LOCKED_BY_PASSWORD = 2;

    //上级领导锁定
    public static final Integer LOCKED_BY_LEADER = 3;

    //密码尝试次数上限
    public static final Integer PASSWORD_ATTEMPT_MAX_COUNT = 5;

    @ApiModelProperty("用户名")
    @Column(unique = true, length = 225, nullable = false)
    @NotNull(message = "用户名不能为空")
    private String name;

    @ApiModelProperty("用户昵称")
    @Column(length = 225)
    private String nickname;

    @ApiModelProperty("密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 225, nullable = false)
    @NotNull(message = "密码不能为空")
    private String password;

    @ApiModelProperty("手机号")
    @Column(unique = true, length = 20, nullable = false)
    @NotNull(message = "手机号不能为空")
    private String phoneNumber;

    @ApiModelProperty("头像")
    @Column(length = 225)
    private String avatar;

    @ApiModelProperty(value = "密码尝试锁定时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockedDate;

    @ApiModelProperty("密码尝试次数")
    @Column(columnDefinition = "int(11) default 0")
    private Integer passwordAttemptCount;

    @ApiModelProperty("备注")
    @Column(length = 225)
    private String remark;

    @ApiModelProperty("角色 'ROLE_SUPER'：超管 'ROLE_USER'：普通用户")
    @Column(length = 225, nullable = false)
    private String role;

    @ApiModelProperty(value = "生日")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty("性别 0：男 1：女 2：未知")
    private Integer gender;

    @ApiModelProperty("邮箱")
    @Column(length = 225)
    private String email;

    @ApiModelProperty("微信名")
    @Column(length = 225)
    private String weChatName;

    @ApiModelProperty("微信OpenID")
    @Column(length = 225)
    private String weChatOpenId;

    @ApiModelProperty("微信unionid")
    @Column(length = 225)
    private String weChatUnionid;

    @ApiModelProperty("用户等级")
    @Column(length = 11)
    private Integer level;

    @ApiModelProperty("用户积分")
    @Column(length = 11)
    private Integer integral;

    @ApiModelProperty("熊币")
    @Column(length = 11)
    private Integer coin;

    @ApiModelProperty("状态 0：删除, 1:正常, 2:密码尝试锁定, 3:上级领导锁定")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public Integer getPasswordAttemptCount() {
        return passwordAttemptCount;
    }

    public void setPasswordAttemptCount(Integer passwordAttemptCount) {
        this.passwordAttemptCount = passwordAttemptCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeChatName() {
        return weChatName;
    }

    public void setWeChatName(String weChatName) {
        this.weChatName = weChatName;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }

    public String getWeChatUnionid() {
        return weChatUnionid;
    }

    public void setWeChatUnionid(String weChatUnionid) {
        this.weChatUnionid = weChatUnionid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void passwordEncoder() {
        this.password = PasswordUtil.encode(this.password);
    }

    public boolean passwordMatches(String password) {
        return PasswordUtil.matches(password, this.password);
    }
}
