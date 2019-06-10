package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "文章-标签 映射")
@Entity
@DynamicUpdate
@DynamicInsert
public class EssayTagMapping extends BaseEntity {

    @ApiModelProperty("文章ID")
    @NotNull(message = "文章id不能为空")
    @Column(length = 11, nullable = false)
    private Integer essayId;

    @ApiModelProperty("标签ID")
    @NotNull(message = "标签id不能为空")
    @Column(length = 11, nullable = false)
    private Integer tagId;

    public EssayTagMapping() {
    }

    public EssayTagMapping(Integer essayId, Integer tagId) {
        this.essayId = essayId;
        this.tagId = tagId;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
