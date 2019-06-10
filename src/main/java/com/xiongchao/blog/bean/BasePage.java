package com.xiongchao.blog.bean;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页模型
 *
 * @author William Guo
 */
public class BasePage {

    @ApiModelProperty("页码")
    Integer page;

    @ApiModelProperty("页大小")
    Integer size;

    @ApiModelProperty(value = "排序规则(ASC/DESC)", allowableValues = "ASC,DESC")
    String direction;

    @ApiModelProperty("排序字段")
    String[] properties;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public Pageable toPageable() {
        page = page != null ? page : 0;
        size = size != null ? size : 9999;
        // 默认倒序
        Sort.Direction dir = Sort.Direction.fromOptionalString(this.direction).orElse(Sort.Direction.DESC);
        // 默认创建时间
        Sort sort = properties != null ? Sort.by(dir, properties) : Sort.by(dir, "createdDate");

        return PageRequest.of(page, size, sort);
    }
}
