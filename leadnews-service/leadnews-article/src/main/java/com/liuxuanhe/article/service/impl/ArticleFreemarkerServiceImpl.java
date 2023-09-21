package com.liuxuanhe.article.service.impl;

import com.liuxuanhe.article.mapper.ApArticleMapper;
import com.liuxuanhe.article.service.ArticleFreemarkerService;
import com.liuxuanhe.common.minio.MinIOFileStorageService;
import com.liuxuanhe.model.article.pojos.ApArticle;
import com.liuxuanhe.model.article.pojos.ApArticleContent;
import com.liuxuanhe.utils.common.JsonUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {
    @Autowired
    private Configuration configuration;
    @Autowired
    private MinIOFileStorageService storageService;
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public void buildArticleToMinIO(ApArticle apArticle, String content) {

        try {
            if(content!=null && StringUtils.isNoneEmpty(content)){
                // 读取文章详情页模板文件
                Template template = configuration.getTemplate("article.ftl");
                Map<String,Object> data = new HashMap<>();
                List<Map> list = JsonUtils.toList(content,Map.class);
                data.put("content",list);


                // 生成文章的静态页面
                StringWriter writer = new StringWriter();
                template.process(data,writer);

                // 文件上传到MinIO
                String fileName = apArticle.getId()+".html";
                InputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes());
                String path = storageService.uploadHtmlFile(null, fileName, inputStream);

                // 把MinIO的文件地址更新到article表中


                apArticle.setStaticUrl(path);
                apArticleMapper.upDate(apArticle);
                log.info("生成文章详情页成功！ID：{}",apArticle.getId());

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成文章详情页失败：{}",e.getMessage());
        }
    }
}
