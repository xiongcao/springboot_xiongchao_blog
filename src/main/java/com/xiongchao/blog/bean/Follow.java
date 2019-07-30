package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;

@ApiModel(description = "关注")
@Entity
@DynamicUpdate
@DynamicInsert
public class Follow extends BaseEntity {

    @ApiModelProperty("头像")
    @Column(length = 225)
    private String avatar;

    @ApiModelProperty("用户名")
    @Column(unique = true, length = 225, nullable = false)
    private String name;

    @ApiModelProperty("用户昵称")
    @Column(length = 225)
    private String nickname;

    @ApiModelProperty("状态 0：删除 1：关注 2：粉丝")
    @Column(length = 11)
    private Integer status;

    @ApiModelProperty("关注用户ID")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    @ApiModelProperty("被关注的用户ID(粉丝)")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer followUserId;

    private Boolean mutualWatch;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(Integer followUserId) {
        this.followUserId = followUserId;
    }

    public Boolean getMutualWatch() {
        return mutualWatch;
    }

    public void setMutualWatch(Boolean mutualWatch) {
        this.mutualWatch = mutualWatch;
    }
}
