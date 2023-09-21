package com.liuxuanhe.article.mapper;

import com.liuxuanhe.model.article.dtos.ArticleDto;
import com.liuxuanhe.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ApArticleMapper {

    List<ApArticle> loadApArticle(@Param("dto") ArticleDto dto, @Param("type") int type);

    void upDate(@Param("apArticle") ApArticle apArticle);

    @Select("select * from ap_article where id = #{id}")
    ApArticle selectTest(Long id);

    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @Insert("insert into ap_article values(#{apArticle.id},#{apArticle.title},#{apArticle.authorId},#{apArticle.authorName},#{apArticle.channelId},#{apArticle.channelName},#{apArticle.layout},#{apArticle.flag},#{apArticle.images},#{apArticle.labels},#{apArticle.likes},#{apArticle.collection},#{apArticle.comment},#{apArticle.views},#{apArticle.provinceId},#{apArticle.cityId},#{apArticle.countyId},#{apArticle.createdTime},#{apArticle.publishTime},#{apArticle.syncStatus},#{apArticle.origin},#{apArticle.staticUrl})")
    void save(@Param("apArticle") ApArticle apArticle);
}
