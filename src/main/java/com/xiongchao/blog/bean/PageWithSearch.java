package com.xiongchao.blog.bean;

import com.xiongchao.blog.util.SqlUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * Created by cachee on 4/27/2018.
 */
public class PageWithSearch extends BasePage{

    @ApiParam("搜索字段名")
    private String field;

    @ApiParam("搜索值")
    private String value;

    @ApiParam("起始时间")
    private String startDate;

    @ApiParam("截止时间")
    private String endDate;

    public Pageable toPageableWithDefault(Integer page, Integer size, Sort.Direction direction, String orderBy, String prefix) {
        this.page = this.page == null ? page : this.page;
        this.size = this.size == null ? size : this.size;
        // 默认倒序
        Sort.Direction dir = Sort.Direction.fromOptionalString(this.direction).orElse(direction);
        // 默认创建时间
        Sort sort = (properties == null || properties.length == 0) ?  Sort.by(dir, prefix + "." + SqlUtil.camelToUnderline(orderBy)) : Sort.by(dir, prefix + "." + SqlUtil.camelToUnderline(this.properties[0]));

        return PageRequest.of(this.page, this.size, sort);
    }

    public Pageable toPageableWithDefault(Integer page, Integer size, Sort.Direction direction, String orderBy) {
        this.page = this.page == null ? page : this.page;
        this.size = this.size == null ? size : this.size;
        // 默认倒序
        Sort.Direction dir = Sort.Direction.fromOptionalString(this.direction).orElse(direction);
        // 默认创建时间
        Sort sort = (properties == null || properties.length == 0) ?  Sort.by(dir, orderBy) : Sort.by(dir, properties);

        return PageRequest.of(this.page, this.size, sort);
    }

    public Pageable toPageableWithDefault(Integer page, Integer size) {
        this.page = this.page == null ? page : this.page;
        this.size = this.size == null ? size : this.size;
        return PageRequest.of(this.page, this.size);
    }

    public String defaultOrderBy(String orderBy) {
        if (null == this.properties || this.properties.length == 0)return orderBy;
        return this.properties[0];
    }

    public String defaultOrderBy(String orderBy, String prefix) {
        if (null == this.properties || this.properties.length == 0)return prefix + "." + SqlUtil.camelToUnderline(orderBy);
        return prefix + "." + SqlUtil.camelToUnderline(this.properties[0]);
    }

    public String defaultDirection(String direction) {
        if(StringUtils.isEmpty(this.direction))return direction;
        return this.direction;
    }

    public Integer defaultPage(Integer page) {
        return this.page == null ? page : this.page;
    }

    public Integer defaultSize(Integer size) {
        return this.size == null ? size : this.size;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
