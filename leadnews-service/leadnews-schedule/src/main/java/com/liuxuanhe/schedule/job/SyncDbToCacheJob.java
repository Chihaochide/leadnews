package com.liuxuanhe.schedule.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.liuxuanhe.common.constants.RedisConstants;
import com.liuxuanhe.model.schedule.dtos.Task;
import com.liuxuanhe.model.schedule.pojos.Taskinfo;
import com.liuxuanhe.schedule.mapper.TaskinfoMapper;
import com.liuxuanhe.utils.common.BeanHelper;
import com.liuxuanhe.utils.common.JsonUtils;
import com.sun.jmx.remote.internal.ArrayQueue;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SyncDbToCacheJob {

    @Autowired
    private TaskinfoMapper taskinfoMapper;
    @Autowired
    private StringRedisTemplate template;

    /**
     * 定时同步MySQL数据到缓存
     */
    @Scheduled(fixedRate = 10000)
    public void syncData(){
        log.info("执行定时同步MySQL数据到缓存，{}",new Date());
        // 查询Mysql中符合条件（未来5分钟内执行的任务）的内容
        Date futureTime = DateTime.now().plusMinutes(5).toDate();
        QueryWrapper<Taskinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("execute_time",futureTime);
        List<Taskinfo> taskinfoList = taskinfoMapper.selectList(queryWrapper);

        // 导入Redis缓存
        if (CollectionUtils.isNotEmpty(taskinfoList)){
            System.out.println("进入Redis");
            for (Taskinfo taskinfo : taskinfoList) {
                String key = RedisConstants.TASK_TOPIC_PREFIX+taskinfo.getTaskTopic();
                Task task = BeanHelper.copyProperties(taskinfo, Task.class);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                template.opsForZSet().add(key, JsonUtils.toString(task),task.getExecuteTime());
            }
        }

    }
}
