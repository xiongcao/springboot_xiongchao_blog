package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "收藏")
@Entity
@DynamicUpdate
@DynamicInsert
public class Collect extends BaseEntity {

    @ApiModelProperty("状态 0：删除 1：正常")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    @ApiModelProperty("用户ID")
    @NotNull(message = "用户id不能为空")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    @ApiModelProperty("文章ID")
    @NotNull(message = "文章id不能为空")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer essayId;

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

    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }
}
