package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "点赞")
@Entity
@DynamicUpdate
@DynamicInsert
public class Star extends BaseEntity {

    @ApiModelProperty("用户ID")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    @ApiModelProperty("文章ID")
    @Column(columnDefinition = "int(11)")
    private Integer essayId;

    @ApiModelProperty("状态 0：取消点赞 1：已点赞 -1：删除")
    @Column(columnDefinition = "int(11) default 0")
    private Integer status;

    @ApiModelProperty("评论ID")
    @Column(columnDefinition = "int(11)")
    private Integer commentId;

    @ApiModelProperty("评论点赞状态 1: 已赞 0: 踩 -1: 未点赞、未踩")
    @Column(columnDefinition = "int(11) default -1")
    private Integer commentLikeStatus;

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

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCommentLikeStatus() {
        return commentLikeStatus;
    }

    public void setCommentLikeStatus(Integer commentLikeStatus) {
        this.commentLikeStatus = commentLikeStatus;
    }
}
