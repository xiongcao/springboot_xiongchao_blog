package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@ApiModel(description = "评论")
@Entity
@DynamicUpdate
@DynamicInsert
public class Comment extends BaseEntity {

    @ApiModelProperty("评论内容")
    @Column(unique = true, length = 10000, nullable = false)
    private String content;

    @ApiModelProperty("父评论ID")
    private Integer pid;

    @ApiModelProperty("被评论者ID")
    @NotNull(message = "被评论者id不能为空")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer toUserId;

    @ApiModelProperty("状态 0：删除 1：正常")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    @ApiModelProperty("文章ID")
    @NotNull(message = "文章id不能为空")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer essayId;

    @ApiModelProperty("评论者ID")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
