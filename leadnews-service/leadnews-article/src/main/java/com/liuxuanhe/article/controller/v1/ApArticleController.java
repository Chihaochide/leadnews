package com.liuxuanhe.article.controller.v1;

import com.liuxuanhe.article.service.ApArticleService;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.article.dtos.ApArticleDto;
import com.liuxuanhe.model.article.dtos.ArticleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class  ApArticleController {
    @Autowired
    private ApArticleService apArticleService;

    /**
     * 首页文章
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleDto dto){
        return apArticleService.loadApArticle(dto,1); // 1 代表小于
    }
    /**
     * 更多文章（从下往上拉）
     * @param dto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleDto dto){
        return apArticleService.loadApArticle(dto,1); // 1 代表小于

    }
    /**
     * 最新文章（从上往下拉）
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleDto dto){
        return apArticleService.loadApArticle(dto,2); // 2 代表大于
    }

    /**
     * 保存app文章
     */
    @PostMapping("/save")
    public Long save(@RequestBody ApArticleDto dto){
        System.err.println("===========================");
        System.err.println("===========================");
        System.err.println("===========================");
        System.err.println("调用到Save方法");
        System.err.println("===========================");
        System.err.println("===========================");
        System.err.println("===========================");

        return apArticleService.saveAPArticle(dto);
    }

}
