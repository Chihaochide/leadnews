package com.liuxuanhe.wemedia.service;

import com.liuxuanhe.common.dtos.PageResponseResult;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.wemedia.dtos.WmNewsDto;
import com.liuxuanhe.model.wemedia.dtos.WmNewsPageReqDto;

public interface WmNewsService {

    PageResponseResult findList(WmNewsPageReqDto dto);

    ResponseResult submit(WmNewsDto dto);

    Object getById(Integer id);

    void down(WmNewsDto wmNewsDto);

    void delNews(Integer id);
}
