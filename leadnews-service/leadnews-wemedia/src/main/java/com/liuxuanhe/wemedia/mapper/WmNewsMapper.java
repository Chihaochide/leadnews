package com.liuxuanhe.wemedia.mapper;

import com.liuxuanhe.model.wemedia.dtos.WmNewsPageReqDto;
import com.liuxuanhe.model.wemedia.pojos.WmNews;
import com.liuxuanhe.model.wemedia.pojos.WmUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface WmNewsMapper {
    List<WmNews> findList(@Param("wmUser") WmUser wmUser, @Param("dto") WmNewsPageReqDto dto);


    void save(@Param("wmNews") WmNews wmNews);

    @Select("select * from wm_news where id = #{id}")
    WmNews getById(Integer id);

    @Update("update wm_news set status = #{wmNews.status},reason=#{wmNews.reason} where id = #{wmNews.id}")
    void updateStatusAndReason(@Param("wmNews") WmNews wmNews);

    @Update("update wm_news set article_id =#{wmNews.articleId} ")
    void updateArticleId(@Param("wmNews") WmNews wmNews);

    void update(@Param("wmNews") WmNews wmNews);

    @Delete("delete from wm_news where id = #{id}")
    void deleteNews(Integer id);
}
