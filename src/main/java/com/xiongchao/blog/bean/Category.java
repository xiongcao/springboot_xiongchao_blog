package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "类型")
@Entity
@DynamicUpdate
@DynamicInsert
public class Category extends BaseEntity {

    @ApiModelProperty("类型名称")
    @Column(unique = true, length = 225, nullable = false)
    private String name;


    @ApiModelProperty("状态 0：删除 1：正常")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    @ApiModelProperty("用户ID")
    @NotNull(message = "用户id不能为空")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
