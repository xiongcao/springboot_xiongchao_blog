package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;

@ApiModel(description = "项目")
@Entity
@DynamicUpdate
@DynamicInsert
public class Project extends BaseEntity {

    @ApiModelProperty("名称")
    @Column(unique = true, length = 225, nullable = false)
    private String name;

    @ApiModelProperty("排序序号")
    @Column(name = "`rank`", columnDefinition = "int(11) default 1")
    private Integer rank;

    @ApiModelProperty("状态 0：隐藏 1：正常 -1：删除")
    @Column(columnDefinition = "int(11) default 1")
    private Integer status;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("项目周期")
    private String cycle;

    @ApiModelProperty("项目链接")
    private String url;

    @ApiModelProperty("介绍")
    @Column(length = 500)
    private String introduction;

    @ApiModelProperty("用户ID")
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
