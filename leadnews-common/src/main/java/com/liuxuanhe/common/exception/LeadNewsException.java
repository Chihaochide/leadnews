package com.liuxuanhe.common.exception;

import com.liuxuanhe.common.dtos.AppHttpCodeEnum;
import lombok.Getter;

/**
 * 业务异常封装类
 */
@Getter
public class LeadNewsException extends RuntimeException {
    private Integer status; //状态码

    public LeadNewsException(Integer status,String message){
        super(message);
        this.status = status;
    }

    public LeadNewsException(AppHttpCodeEnum codeEnum){
        super(codeEnum.getErrorMessage());
        this.status = codeEnum.getCode();
    }

}
