package com.liuxuanhe.article.mapper;

import com.liuxuanhe.model.article.pojos.ApArticleContent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ApArticleContentMapper {

    @Select("select * from ap_article_content where article_id = #{articleId}")
    ApArticleContent queryById(Long articleId);


    void upDate(@Param("apArticleContent") ApArticleContent apArticleContent);


    @Insert("insert into ap_article_content values(#{apArticleContent.id},#{apArticleContent.articleId},#{apArticleContent.content})")
    void save(@Param("apArticleContent") ApArticleContent apArticleContent);
}
