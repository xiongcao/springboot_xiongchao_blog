package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "文章-类型映射")
@Entity
@DynamicUpdate
@DynamicInsert
public class EssayCategoryMapping extends BaseEntity {

    @ApiModelProperty("文章ID")
    @NotNull(message = "文章id不能为空")
    @Column(length = 11, nullable = false)
    private Integer essayId;

    @ApiModelProperty("类型ID")
    @NotNull(message = "类型id不能为空")
    @Column(length = 11, nullable = false)
    private Integer categoryId;

    public EssayCategoryMapping(){}

    public EssayCategoryMapping(Integer essayId, Integer categoryId) {
        this.essayId = essayId;
        this.categoryId = categoryId;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
