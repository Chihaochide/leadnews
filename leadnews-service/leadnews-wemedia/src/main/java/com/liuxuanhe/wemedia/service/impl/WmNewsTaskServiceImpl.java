package com.liuxuanhe.wemedia.service.impl;

import com.liuxuanhe.common.constants.ScheduleConstants;
import com.liuxuanhe.model.schedule.dtos.Task;
import com.liuxuanhe.model.wemedia.pojos.WmNews;
import com.liuxuanhe.schedule.feign.TaskFeign;
import com.liuxuanhe.utils.common.JsonUtils;
import com.liuxuanhe.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {


    @Autowired
    private TaskFeign taskFeign;

    @Override
    public Long addWmNewsTask(WmNews wmNews) {
        Task task = new Task();
        task.setExecuteTime(wmNews.getPublishTime().getTime()); //文章的发布时间，作为任务的执行时间
        task.setTaskTopic(ScheduleConstants.TASK_TOPIC_NEWS_PUBLISH);
        WmNews news =  new WmNews();
        news.setId(wmNews.getId());
        task.setParameters(JsonUtils.toString(news));

        Long taskId = taskFeign.addTask(task);
        log.info("任务添加到延迟队列中，ID：{}",taskId);

        return taskId;
    }
}
