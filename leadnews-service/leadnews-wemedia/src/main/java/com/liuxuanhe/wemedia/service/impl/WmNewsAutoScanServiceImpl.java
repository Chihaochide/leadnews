package com.liuxuanhe.wemedia.service.impl;

import com.aliyun.imageaudit20191230.Client;
import com.aliyun.imageaudit20191230.models.ScanTextRequest;
import com.aliyun.imageaudit20191230.models.ScanTextResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuxuanhe.article.feign.ApArticleFeign;
import com.liuxuanhe.common.aliyun.Sample;
import com.liuxuanhe.common.constants.RedisConstants;
import com.liuxuanhe.model.article.dtos.ApArticleDto;
import com.liuxuanhe.model.wemedia.pojos.*;
import com.liuxuanhe.utils.common.BeanHelper;
import com.liuxuanhe.utils.common.JsonUtils;
import com.liuxuanhe.utils.common.SensitiveWordUtil;
import com.liuxuanhe.wemedia.mapper.WmChannelMapper;
import com.liuxuanhe.wemedia.mapper.WmNewsMapper;
import com.liuxuanhe.wemedia.mapper.WmSensitiveMapper;
import com.liuxuanhe.wemedia.mapper.WmUserMapper;
import com.liuxuanhe.wemedia.service.WmNewsAutoScanService;
import com.liuxuanhe.wemedia.service.WmNewsTaskService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Value("${aliyun.AccessKeyID}")
    private String AccessKeyID;
    @Value("${aliyun.AccessKeySecret}")
    private String AccessKeySecret;
    @Autowired
    private ApArticleFeign apArticleFeign;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private WmNewsTaskService taskService;

    @Override
    @GlobalTransactional
    public void autoScanWmNews(Integer id) {
        System.out.println("自动审核启动，开始审核！！！！");
        // 查询自媒体文章
        WmNews wmNews = wmNewsMapper.getById(id);
        if (wmNews==null) return;
        if (wmNews.getStatus()!=1) return;
        // 提取文章中的文字
        List<String> text = getTextFromWmNews(wmNews);
        // 提取文章中的图片（没做）
        // 封面的图片路径（没做）


        // 自定义的敏感词
        if (!CollectionUtils.isEmpty(text)){
            boolean flag = handleSensitiveResult(text,wmNews);
            if (!flag){
                return;
            }
        }
        int []a;
        int b[];

        // 提交给阿里云
        try {
            Client client = Sample.createClient(AccessKeyID, AccessKeySecret);

            ScanTextRequest.ScanTextRequestTasks scanTextRequestTasks = new ScanTextRequest.ScanTextRequestTasks();
            List<ScanTextRequest.ScanTextRequestLabels> labels = Sample.getLabels();
            List<ScanTextRequest.ScanTextRequestTasks> tasksList = Sample.getTasksList(text);
            ScanTextRequest scanTextRequest = new ScanTextRequest().setTasks(tasksList).setLabels(labels);
            RuntimeOptions runTime = new RuntimeOptions();
            ScanTextResponse response = client.scanTextWithOptions(scanTextRequest, runTime);
            Map<String, Object> map = response.getBody().getData().toMap();
            List<String> suggestion = Sample.getSuggestion(response);
            boolean flag = handleScanResult(suggestion, wmNews);
            if (!flag){
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("阿里云接口调用出现错误：{}",e.getMessage());
        }

        System.out.println(wmNews.getPublishTime());
        // 判断用户选择的发布时间是否大于当前时间，如果大于则标记8（代表暂不发布）
        if (wmNews.getPublishTime()!=null && wmNews.getPublishTime().after(new Date())){
            // 暂时不发布
            wmNews.setStatus(WmNews.Status.SUCCESS.getCode());
            wmNews.setReason("已审核通过，进入定时发布！");
            wmNewsMapper.updateStatusAndReason(wmNews);

            // 把文章定时发布的任务添加到延迟队列
            taskService.addWmNewsTask(wmNews);
            return;
        }

        // 如果发布时间小于等于当前时间，立即发布（把文章存入app端的库中，修改文章状态为9）
        wmNews.setPublishTime(new Date());
        publishApArticle(wmNews);

    }

    private boolean handleSensitiveResult(List<String> text, WmNews wmNews) {
        boolean flag = true;
        // 从redis查询所有的敏感词
        String redisData = redisTemplate.opsForValue().get(RedisConstants.SENSITIVE_WORD);
        List<String> wordList = null;
        ObjectMapper mapper = new ObjectMapper();

        if (StringUtils.isEmpty(redisData)){
            // 查询DB
            List<WmSensitive> wmSensitives = wmSensitiveMapper.findAll();
            if (!CollectionUtils.isEmpty(wmSensitives)){
                try {
                    wordList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
                    // 存入redis
                    String wordListJson = mapper.writeValueAsString(wordList);
                    redisTemplate.opsForValue().set(RedisConstants.SENSITIVE_WORD,wordListJson);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }else {
            try {
                wordList = mapper.readValue(redisData, mapper.getTypeFactory().constructType(List.class, String.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (!CollectionUtils.isEmpty(wordList)){
            // 初始化DFA词库
            SensitiveWordUtil.initMap(wordList);
            // 使用文章文字内容匹配DFA词库
            String content = filterWords(text.stream().collect(Collectors.joining("")));
            Map<String, Integer> result = SensitiveWordUtil.matchWords(content);
            // 根据结果修改文章
            if ( result!=null && result.size()>0 ){
                // 出现了违规词
                wmNews.setStatus(WmNews.Status.FAIL.getCode());
                wmNews.setReason("文章出现违规词"+result.keySet());
                wmNewsMapper.updateStatusAndReason(wmNews);
                flag = false;
            }
        }

        return flag;
    }


    /**
     * 过滤内容
     * @param collect
     * @return
     */
    private String filterWords(String collect) {
        // 去掉空格
        String content = collect.replaceAll(" ", "");
        // 去掉特殊符号 -=* 【没去】
        return content;
    }

    /**
     * 发布文章
     * @param wmNews
     */
    public void publishApArticle(WmNews wmNews) {
        ApArticleDto dto = BeanHelper.copyProperties(wmNews,ApArticleDto.class);
        // 把自媒体的articleId赋值给dto
        dto.setId(wmNews.getArticleId());

        WmUser wmUser  = wmUserMapper.selectByUserId(wmNews.getUserId());
        if (wmUser!=null){
            dto.setAuthorId(Long.valueOf(wmUser.getId()));
            dto.setAuthorName(wmUser.getNickname());
        }
        dto.setLayout(wmNews.getType());
        WmChannel channel = wmChannelMapper.selectById(wmNews.getChannelId());
        if (channel!=null){
            dto.setChannelId(channel.getId());
            dto.setChannelName(channel.getName());
        }
        // 初始化文章行为数据（点赞数量、收藏数量、评论数量、阅读数量）
        dto.setLikes(0);
        dto.setViews(0);
        dto.setComment(0);
        dto.setCollection(0);
        Long articleId = apArticleFeign.save(dto);


        if (articleId!=null){
            // 修改状态为9 已发布
            wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
            wmNews.setReason("文章已发布成功！");
            wmNewsMapper.updateStatusAndReason(wmNews);
            // 记录发布到App库时的id
            wmNews.setArticleId(articleId);
            wmNewsMapper.updateArticleId(wmNews);
        }
    }


    /**
     * 处理检测结果
     * @param suggestion
     * @param wmNews
     */
    private boolean handleScanResult(List<String> suggestion, WmNews wmNews) {
        for (String s : suggestion) {
            if ("block".equals(s)){
                // 修改文章
                wmNews.setStatus(WmNews.Status.FAIL.getCode());
                wmNews.setReason("文章包含违规内容，请检查");
                wmNewsMapper.updateStatusAndReason(wmNews);
                return false;
            }
            if ("review".equals(s)){
                // 修改文章
                wmNews.setStatus(WmNews.Status.ADMIN_AUTH.getCode());
                wmNews.setReason("人工审核，请检查");
                wmNewsMapper.updateStatusAndReason(wmNews);
                return false;
            }
        }
        return true;
    }

    /**
     * 提取文章的文字
     * @param wmNews
     * @return
     */
    private List<String> getTextFromWmNews(WmNews wmNews) {
        List<String> textList = new ArrayList<>();
        // 标题
        if (!StringUtils.isEmpty(wmNews.getTitle())){
            textList.add(wmNews.getTitle());
        }
        // 内容
        if (!StringUtils.isEmpty(wmNews.getContent())){
            List<Map> list = JsonUtils.toList(wmNews.getContent(), Map.class);
            for (Map map :list){
                if (map.get("type").equals("text")){
                    textList.add((String) map.get("value"));
                }
            }
        }
        // 标签
        if (!StringUtils.isEmpty(wmNews.getContent())){
            textList.add(wmNews.getLabels());
        }
        return textList;
    }
}
