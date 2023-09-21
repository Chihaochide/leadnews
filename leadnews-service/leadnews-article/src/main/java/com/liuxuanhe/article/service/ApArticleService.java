package com.liuxuanhe.article.service;

import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.article.dtos.ApArticleDto;
import com.liuxuanhe.model.article.dtos.ArticleDto;

public interface ApArticleService {
    ResponseResult loadApArticle(ArticleDto dto, int i);

    /**
     * 保存文章（新增还是修改）
     */
    Long saveAPArticle(ApArticleDto dto);


}
