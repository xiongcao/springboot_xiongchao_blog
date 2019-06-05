package com.xiongchao.blog.exception;

import com.xiongchao.blog.bean.BaseResult;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 统一异常处理
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 未登录异常拦截
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResult handle(ServletRequestBindingException e) {
        logger.error("用户登录状态失效");
        return BaseResult.failure(BaseResult.NOLOGIN, "未登录");
    }

    /**
     * 参数验证异常拦截
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数", e);
        return BaseResult.failure(BaseResult.PARAMETER_ERROR, "缺少请求参数");
    }

    /**
     * 参数验证异常拦截
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return BaseResult.failure(BaseResult.PARAMETER_ERROR, "参数解析失败");
    }

    /**
     * 参数验证异常拦截
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult handleServiceException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        logger.error("参数验证失败 : {}", message, e);
        return BaseResult.failure(BaseResult.PARAMETER_ERROR, "参数验证失败 : " + message);
    }

    /**
     * 参数验证异常拦截
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public BaseResult handleValidationException(ValidationException e) {
        logger.error("参数验证失败 : ", e);
        return BaseResult.failure(BaseResult.PARAMETER_ERROR, "参数验证失败");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return BaseResult.failure(BaseResult.PARAMETER_ERROR, message);
    }

    /**
     * 500 - INTERNAL_SERVER_ERROR
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FeignException.class)
    public BaseResult handleMethodFeignException(FeignException e) {
        logger.error("参数验证失败", e);
        return BaseResult.failure(BaseResult.PARAMETER_ERROR, e.getMessage());
    }

    /**
     * 500 - INTERNAL_SERVER_ERROR
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseResult runtimeException(RuntimeException e) {
        return BaseResult.failure(BaseResult.FAILURE, e.getMessage());
    }

}
