package com.liuxuanhe.wemedia.mapper;

import com.liuxuanhe.model.wemedia.dtos.WmLoginDto;
import com.liuxuanhe.model.wemedia.pojos.WmUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface WmUserMapper {
    @Select("select * from wm_user where name = #{dto.name}")
    WmUser findUserByName(@Param("dto") WmLoginDto dto);

    @Select("select * from wm_user where id = #{userId}")
    WmUser selectByUserId(@Param("userId") Integer userId);
}
