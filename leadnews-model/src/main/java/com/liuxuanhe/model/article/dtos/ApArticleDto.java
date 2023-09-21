package com.liuxuanhe.model.article.dtos;

import com.liuxuanhe.model.article.pojos.ApArticle;
import lombok.Data;

/**
 * 在自媒体端传递到App端的数据对象
 */
@Data
public class ApArticleDto extends ApArticle {
    private String content;
}