package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "文章-评论映射")
@Entity
@DynamicUpdate
@DynamicInsert
public class EssayCommentMapping extends BaseEntity {

    @ApiModelProperty("文章ID")
    @NotNull(message = "文章id不能为空")
    @Column(length = 11, nullable = false)
    private Integer essayId;

    @ApiModelProperty("评论ID")
    @NotNull(message = "评论id不能为空")
    @Column(length = 11, nullable = false)
    private Integer commentId;

    public EssayCommentMapping(){}

    public EssayCommentMapping(Integer essayId, Integer commentId) {
        this.essayId = essayId;
        this.commentId = commentId;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
}
