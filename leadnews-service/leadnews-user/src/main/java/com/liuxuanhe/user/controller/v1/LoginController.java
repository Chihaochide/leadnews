package com.liuxuanhe.user.controller.v1;

import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liuxuanhe.model.user.dtos.LoginDto;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {
    @Autowired
    private ApUserService apUserService;
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto dto){

        return apUserService.login(dto);
    }
}
