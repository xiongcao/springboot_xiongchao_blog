package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;

@ApiModel(description = "文章")
@Entity
@DynamicUpdate
@DynamicInsert
public class Essay extends BaseEntity {

    @ApiModelProperty("标题")
    @Column(unique = true, length = 225, nullable = false)
    private String title;

    @ApiModelProperty("标签 0：原创 1：转发")
    @Column(columnDefinition = "int(11) default 0")
    private Integer type;

    @ApiModelProperty("正文")
    @Column(length = 20000)
    private String content;

    @ApiModelProperty("浏览次数")
    @Column(length = 11)
    private Integer browseNumber;

    @ApiModelProperty("序号")
    @Column(columnDefinition = "int(11) default 0")
    private Integer rank;

    @ApiModelProperty("状态 0：删除 1：正常")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    @ApiModelProperty("用户ID")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(Integer browseNumber) {
        this.browseNumber = browseNumber;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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
