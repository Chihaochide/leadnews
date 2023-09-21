package com.liuxuanhe.article.mapper;

import com.liuxuanhe.model.article.pojos.ApArticleConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ApArticleConfigMapper {
    @Insert("insert into ap_article_config values(#{apArticleConfig.id},#{apArticleConfig.articleId}," +
            "#{apArticleConfig.isComment},#{apArticleConfig.isForward},#{apArticleConfig.isDown}," +
            "#{apArticleConfig.isDelete})")
    void save(@Param("apArticleConfig") ApArticleConfig apArticleConfig);
}
