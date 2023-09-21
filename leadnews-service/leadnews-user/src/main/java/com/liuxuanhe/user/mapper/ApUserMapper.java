package com.liuxuanhe.user.mapper;

import com.liuxuanhe.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApUserMapper {

    @Select("select * from ap_user where phone = #{phone}")
    ApUser findUserByPhone(String phone);
}
