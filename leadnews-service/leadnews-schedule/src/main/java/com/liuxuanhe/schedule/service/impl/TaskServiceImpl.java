package com.liuxuanhe.schedule.service.impl;

import cn.hutool.core.date.StopWatch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuxuanhe.common.constants.RedisConstants;
import com.liuxuanhe.common.constants.ScheduleConstants;
import com.liuxuanhe.model.schedule.dtos.Task;
import com.liuxuanhe.model.schedule.pojos.Taskinfo;
import com.liuxuanhe.model.schedule.pojos.TaskinfoLogs;
import com.liuxuanhe.schedule.mapper.TaskinfoLogsMapper;
import com.liuxuanhe.schedule.mapper.TaskinfoMapper;
import com.liuxuanhe.schedule.service.TaskService;
import com.liuxuanhe.utils.common.BeanHelper;

import com.liuxuanhe.utils.common.JsonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {


    @Autowired
    private TaskinfoMapper taskinfoMapper;
    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;
    @Autowired
    private StringRedisTemplate template;

    @Override
    @Transactional
    public Long addTask(Task task) {

        // 任务添加到数据库
        addTaskToDb(task);
        // 任务添加到缓存
        addTaskToCache(task);

        // 返回任务ID

        return task.getTaskId();
    }

    /**
     * 添加任务到缓存
     * @param task
     */
    private void addTaskToCache(Task task) {
        // 判断当前任务的执行时间是否在未来5分钟之内
        long futureTime = DateTime.now().plusMillis(5).getMillis();
        if (task.getExecuteTime()<futureTime){
            String key = RedisConstants.TASK_TOPIC_PREFIX+task.getTaskTopic(); // 区分不同的任务类型
            ObjectMapper mapper = new ObjectMapper();
            try {
                template.opsForZSet().add(key,mapper.writeValueAsString(task),task.getExecuteTime()); // 把任务的执行时间作为score；
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加数据到Db
     * @param task
     */
    private void addTaskToDb(Task task) {
        try {
            //添加任务表
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task,taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);
            // 设置任务ID
            task.setTaskId(taskinfo.getTaskId());


            // 添加任务日志表
            TaskinfoLogs taskinfoLogs = BeanHelper.copyProperties(taskinfo,TaskinfoLogs.class);
            taskinfoLogs.setVersion(1); // 设置初始版本号
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
        } catch (BeansException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List<Task> pollTask(Integer taskTopic) {
        String key = RedisConstants.TASK_TOPIC_PREFIX+taskTopic;
        // 查询redis中sortedSet列表中符合执行时间的任务
        Set<String> taskSet = template.opsForZSet().rangeByScore(key, 0, System.currentTimeMillis());// 查询比当前时间小的元素
        List<Task> taskList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskSet)){
            ObjectMapper mapper = new ObjectMapper();
            for (String taskJson:taskSet){
                try {
                    Task task = mapper.readValue(taskJson, Task.class);

                    // 移除DB的任务
                    updateTaskFromDb(task);
                    // 移除缓存的任务
                    template.opsForZSet().remove(key,taskJson);
                    taskList.add(task);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return taskList;
    }

    private void updateTaskFromDb(Task task) {
        try {
            // 删除任务表的任务
            taskinfoMapper.deleteById(task.getTaskId());
            // 修改任务表日志的状态
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(task.getTaskId());
            taskinfoLogs.setStatus(ScheduleConstants.EXECUTED);
            taskinfoLogsMapper.updateById(taskinfoLogs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }





}
