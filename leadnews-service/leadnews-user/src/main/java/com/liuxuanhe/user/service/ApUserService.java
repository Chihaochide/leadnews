package com.liuxuanhe.user.service;

import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.user.dtos.LoginDto;

public interface ApUserService {
    ResponseResult login(LoginDto dto);
}
