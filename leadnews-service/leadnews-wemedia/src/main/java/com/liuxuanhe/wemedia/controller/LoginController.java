package com.liuxuanhe.wemedia.controller;

import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.dtos.WmLoginDto;
import com.liuxuanhe.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private WmUserService wmUserService;
    /**
     * 自媒体登录
     */
    @PostMapping("/login/in")
    public ResponseResult login(@RequestBody WmLoginDto dto){
        System.out.println("123");
        return wmUserService.login(dto);
    }
}
