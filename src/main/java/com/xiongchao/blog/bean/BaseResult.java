package com.xiongchao.blog.bean;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class BaseResult {
    private static final long serialVersionUID = 1L;

    // 未登录标识
    public final static Integer NOLOGIN              = -1;
    // 成功标识
    public final static Integer SUCCESS              = 0;
    // 失败标识
    public final static Integer FAILURE              = 1;
    // 参数异常
    public final static Integer PARAMETER_ERROR      = 2;
    // 未绑定
    public final static Integer USER_NO_BIND         = 3;
    // 重复绑定
    public static final Integer USER_REPEAT_BIND     = 4;
    // 店铺锁定
    public static final Integer USER_STORE_LOCKED    = 5;
    // 身份校验失败
    public static final Integer ADDRESS_AUTH_FAILURE = 6;

    private Integer code;
    private String  msg;
    private Object  data;

    public BaseResult() {
    }

    public BaseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T> T parseDataToObject(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(this.data), clazz);
    }

    public <T> List<T> parseDataToArray(Class<T> clazz) {
        return JSON.parseArray(JSON.toJSONString(this.data), clazz);
    }

    public static BaseResult success(String msg, Object data) {
        return new BaseResult(SUCCESS, msg, data);
    }

    public static BaseResult success(Object data) {
        return success("成功", data);
    }

    public static BaseResult success() {
        return success(null);
    }

    public static BaseResult failure(Integer code, String msg, Object data) {
        return new BaseResult(code, msg, data);
    }

    public static BaseResult failure(Integer code, String msg) {
        return failure(code, msg, null);
    }

    public static BaseResult failure(String msg, Object data) {
        return failure(FAILURE, msg, data);
    }

    public static BaseResult failure(String msg) {
        return failure(FAILURE, msg);
    }
}
