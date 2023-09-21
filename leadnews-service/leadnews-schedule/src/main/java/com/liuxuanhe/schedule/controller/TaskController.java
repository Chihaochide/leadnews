package com.liuxuanhe.schedule.controller;

import com.liuxuanhe.model.schedule.dtos.Task;
import com.liuxuanhe.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {


    @Autowired
    private TaskService taskService;
    /**
     * 添加延迟队列任务
     */
    @PostMapping("/addTask")
    public Long addTask(@RequestBody Task task){
        return taskService.addTask(task);
    }

    /**
     * 消费任务
     */
    @PostMapping("/pollTask/{taskTopic}")
    public List<Task> pollTask(@PathVariable("taskTopic") Integer taskTopic){
        return taskService.pollTask(taskTopic);
    }
}
