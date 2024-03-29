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

    @ApiModelProperty("简要描述")
    private String des;

    @ApiModelProperty("正文")
    @Column(length = 20000)
    private String content;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("浏览次数")
    @Column(length = 11, columnDefinition = "int(11) default 0")
    private Integer browseNumber;

    @ApiModelProperty("被收藏数")
    @Column(columnDefinition = "int(11) default 0")
    private Integer collectCount;

    @ApiModelProperty("点赞数")
    @Column(columnDefinition = "int(11) default 0")
    private Integer starCount;

    @ApiModelProperty("序号")
    @Column(name = "`rank`", columnDefinition = "int(11) default 0")
    private Integer rank;

    @ApiModelProperty("状态 1：公开  2：私密 3：草稿 4: 自序 0：删除")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    @ApiModelProperty("用户ID")
    @Column(columnDefinition = "int(11)", nullable = false)
    private Integer userId;

    @ApiModelProperty("转发用到的，原文ID")
    @Column(columnDefinition = "int(11)")
    private Integer pid;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getStarCount() {
        return starCount;
    }

    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
