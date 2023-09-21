package com.liuxuanhe.wemedia.service;

import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.dtos.WmLoginDto;

public interface WmUserService {
    ResponseResult login(WmLoginDto dto);
}
