package com.liuxuanhe.wemedia.service;

import com.liuxuanhe.model.wemedia.pojos.WmNews;

public interface WmNewsTaskService {

    /**
     * 添加自媒体定时发布延迟任务
     */
    Long addWmNewsTask(WmNews wmNews);
}
