package com.liuxuanhe.wemedia.service;

import com.liuxuanhe.common.dtos.PageResponseResult;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.dtos.WmMaterialDto;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService {
    ResponseResult uploadPicture(MultipartFile multipartFile);

    PageResponseResult list(WmMaterialDto dto);

    void collect(int id);
}
