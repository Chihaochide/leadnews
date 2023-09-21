package com.liuxuanhe.wemedia.mapper;

import com.liuxuanhe.model.wemedia.pojos.WmSensitive;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WmSensitiveMapper {
    @Select("select * from wm_sensitive")
    List<WmSensitive> findAll();

}
