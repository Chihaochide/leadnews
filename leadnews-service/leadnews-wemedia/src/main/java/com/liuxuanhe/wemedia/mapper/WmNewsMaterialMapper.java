package com.liuxuanhe.wemedia.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WmNewsMaterialMapper {

    @Delete("delete from wm_news_material where news_id = #{newsId}")
    void deleteFromNewsId(Integer newsId);

    void saveNewsMaterials(@Param("materialIds") List<Integer> materialIDs, @Param("newsId") Integer id, @Param("type") int type);
}
