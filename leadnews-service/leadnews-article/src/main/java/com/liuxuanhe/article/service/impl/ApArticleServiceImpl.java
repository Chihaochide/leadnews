package com.liuxuanhe.article.service.impl;

import com.liuxuanhe.article.mapper.ApArticleConfigMapper;
import com.liuxuanhe.article.mapper.ApArticleContentMapper;
import com.liuxuanhe.article.mapper.ApArticleMapper;
import com.liuxuanhe.article.service.ApArticleService;
import com.liuxuanhe.article.service.ArticleFreemarkerService;
import com.liuxuanhe.common.dtos.ResponseResult;
import com.liuxuanhe.model.article.dtos.ApArticleDto;
import com.liuxuanhe.model.article.dtos.ArticleDto;
import com.liuxuanhe.model.article.pojos.ApArticle;
import com.liuxuanhe.model.article.pojos.ApArticleConfig;
import com.liuxuanhe.model.article.pojos.ApArticleContent;
import com.liuxuanhe.utils.common.IdGeneratorSnowflake;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApArticleServiceImpl implements ApArticleService {
    @Autowired(required = false)
    private ApArticleMapper apArticleMapper;
    @Autowired(required = false)
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired(required = false)
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;

    @Override
    public ResponseResult loadApArticle(ArticleDto dto, int type) {
        if (dto.getMinBehotTime()==null) dto.setMinBehotTime(new Date());
        if (dto.getMaxBehotTime()==null) dto.setMaxBehotTime(new Date());
        if (dto.getSize()==null) dto.setSize(10);
        if (dto.getTag()==null) dto.setTag("__all__");

        List<ApArticle> articleList = apArticleMapper.loadApArticle(dto,type);
        return ResponseResult.okResult(articleList);
    }


    @Override
    public Long saveAPArticle(ApArticleDto dto) {
        ApArticle apArticle = new ApArticle();
        IdGeneratorSnowflake idGeneratorSnowflake = new IdGeneratorSnowflake();
        BeanUtils.copyProperties(dto,apArticle);
        apArticle.setPublishTime(new Date());
        // 判断新增或者修改
        if (dto.getId()==null){
            // 新增
            // 新增ap_article表
            apArticle.setId(idGeneratorSnowflake.snowflakeId());
            apArticleMapper.save(apArticle); // 注意做主键数据回显！

            // 新增ap_article_config表
            ApArticleConfig apArticleConfig = new ApArticleConfig();
            apArticleConfig.setId(idGeneratorSnowflake.snowflakeId());
            apArticleConfig.setArticleId(apArticle.getId());
            apArticleConfig.setIsForward(true);
            apArticleConfig.setIsComment(true);
            apArticleConfig.setIsDown(false);
            apArticleConfig.setIsDelete(false);
            apArticleConfigMapper.save(apArticleConfig);
            // 新增ap_article_content表
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setId(idGeneratorSnowflake.snowflakeId());
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.save(apArticleContent);
        }else {
            // 修改
            // 修改ap_article表
            apArticleMapper.upDate(apArticle);
            // 修改ap_content表
            ApArticleContent apArticleContent = apArticleContentMapper.queryById(apArticle.getId());
            apArticleContentMapper.upDate(apArticleContent);
        }
        // 生成文章详情页
        articleFreemarkerService.buildArticleToMinIO(apArticle, dto.getContent());

        return apArticle.getId();
    }
}
