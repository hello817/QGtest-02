package com.qgtest.diary.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //loggerfactory告诉日志是哪个类出的问题
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //BizException错误响应
    @ExceptionHandler(BizException.class)
    public Result<String> BizExceptionHandler(BizException e){
        log.warn("业务异常:{}",e.getMessage());
        return Result.erro(e.getCode(),e.getMessage());
    }
    //Exception，处理除了biz以外的所有错误，用来兜底
    @ExceptionHandler(Exception.class)
    public Result<String> ExceptionHandler(Exception e){
        log.error("系统异常",e);
        return Result.erro("系统繁忙，请稍后再试");
    }
}
