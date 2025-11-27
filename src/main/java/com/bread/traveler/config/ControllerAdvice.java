package com.bread.traveler.config;

import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.bread.traveler.controller")
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBusinessException(Exception e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.businessError(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception e) {
        String message = e.getMessage();
        log.error("服务器系统异常: {}", message, e);
        return Result.serverError(message == null || message.isEmpty() ? "服务器系统异常" : message);
    }

}
