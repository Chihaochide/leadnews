package com.liuxuanhe.wemedia.job;

import com.liuxuanhe.common.constants.ScheduleConstants;
import com.liuxuanhe.model.schedule.dtos.Task;
import com.liuxuanhe.model.wemedia.pojos.WmNews;
import com.liuxuanhe.schedule.feign.TaskFeign;
import com.liuxuanhe.utils.common.JsonUtils;
import com.liuxuanhe.wemedia.mapper.WmNewsMapper;
import com.liuxuanhe.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 从延迟队列中消费--定时发布文章
 */
@Component
@Slf4j
public class WmNewsTaskJob {

    @Autowired
    private TaskFeign taskFeign;
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
    @Autowired
    private WmNewsMapper wmNewsMapper;
    /**
     * 消费定时发布的文章
     */
    @Scheduled(fixedRate = 1000)
    public void pollWmNewsTask(){
        // 拉取任务
        List<Task> taskList = taskFeign.pollTask(ScheduleConstants.TASK_TOPIC_NEWS_PUBLISH);
        if (!CollectionUtils.isEmpty(taskList)){
            for (Task task : taskList) {
                String json = task.getParameters();
                WmNews wmNews = JsonUtils.toBean(json, WmNews.class);
                // 根据Id查询
                wmNews = wmNewsMapper.getById(wmNews.getId());
                wmNewsAutoScanService.publishApArticle(wmNews);
                log.info("文章定时发布完成！ID：{}",wmNews.getId());
            }
        }

    }

}
