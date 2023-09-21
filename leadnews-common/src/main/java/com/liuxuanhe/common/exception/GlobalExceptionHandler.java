package com.heima.common.exception;

import com.liuxuanhe.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  全局异常处理
 */
@RestControllerAdvice  // 底层是AOP切面
public class GlobalExceptionHandler  {

    /**
     * 捕获LeadNewsException
     */
    @ExceptionHandler(value = com.liuxuanhe.common.exception.LeadNewsException.class)
    public ResponseResult leadNewsExceptionHandler(com.liuxuanhe.common.exception.LeadNewsException e){
        return ResponseResult.errorResult(e.getStatus(),e.getMessage());
    }

    /**
     * 系统异常（个人建议开发的时候别写 项目上线了再写）
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseResult exceptionHandler(Exception e){
//        return ResponseResult.errorResult(500,"服务器繁忙！");
//    }

}
