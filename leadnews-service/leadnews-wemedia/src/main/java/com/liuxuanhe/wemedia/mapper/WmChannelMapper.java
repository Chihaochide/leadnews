package com.liuxuanhe.wemedia.mapper;

import com.liuxuanhe.model.wemedia.pojos.WmChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

public interface WmChannelMapper {
    @Select("select * from wm_channel")
    ArrayList<WmChannel> getList();

    @Select("select * from wm_channel where id = #{channelId}")
    WmChannel selectById(@Param("channelId") Integer channelId);
}
