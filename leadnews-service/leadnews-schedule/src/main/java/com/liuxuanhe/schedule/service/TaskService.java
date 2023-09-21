package com.liuxuanhe.schedule.service;

import com.liuxuanhe.model.schedule.dtos.Task;

import java.util.List;

public interface TaskService {

    /**
     * 添加任务
     * 返回值：新增任务的id
     */
    public Long addTask(Task task);

    /**
     * 消费任务
     */
    public List<Task> pollTask(Integer taskTopic);

    /**
     * 取消任务 待实现
     *  1）删除任务表记录，修改任务日志表状态（2）
     *  2）删除缓存记录
     * 参数：任务类型、任务ID
     * 参数：任务类型，任务ID
     */
}
