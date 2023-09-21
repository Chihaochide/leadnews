package com.liuxuanhe.article.feign;

import com.liuxuanhe.model.article.dtos.ApArticleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * name: 需要调用的服务名称
 * path：统一访问路径
 */

/**
 * Feign接口注意事项：
 *  1）所有形参都必须添加注解！！！（不能省略注解）
 *  2）返回对象（List）必须声明泛型类型：List<ApArticle>
 */
@FeignClient(name="leadnews-article",path = "/api/v1/article")
public interface ApArticleFeign {


    /**
     * 保存app文章
     */
    @PostMapping("/save")
    Long save(@RequestBody ApArticleDto dto);
}
