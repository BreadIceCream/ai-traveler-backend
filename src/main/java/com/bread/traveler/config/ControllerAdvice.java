package com.bread.traveler.config;

import com.bread.traveler.exception.BusinessException;
import com.bread.traveler.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice(basePackages = "com.bread.traveler.controller")
@Slf4j
public class ControllerAdvice {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBusinessException(Exception e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.businessError(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件上传过大: {}", e.getMessage());
        return Result.businessError("上传文件过大，请上传小于%s的文件".formatted(maxFileSize));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception e) {
        String message = e.getMessage();
        log.error("服务器系统异常: {}", message, e);
        return Result.serverError(message == null || message.isEmpty() ? "服务器系统异常" : message);
    }

}
