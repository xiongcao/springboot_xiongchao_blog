package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "用户")
@Entity
@DynamicUpdate
@DynamicInsert
public class User extends BaseEntity {

    @ApiModelProperty("用户名")
    @Column(unique = true, length = 50)
    @NotNull(message = "用户名不能为空")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
