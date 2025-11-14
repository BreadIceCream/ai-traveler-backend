package com.bread.traveler.config;

import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.bread.traveler.controller")
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.businessError(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("服务器系统异常: {}", e.getMessage(), e);
        return Result.serverError("系统异常");
    }

}
