package com.liuxuanhe.utils.common;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IdGeneratorSnowflake {
    private long workedId = 0;
    private long datacenterId = 1;
    private Snowflake snowflake = IdUtil.createSnowflake(workedId,datacenterId);

    @PostConstruct
    public void init(){
        try {
            workedId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            System.out.println("当前机器的workedId = " + workedId);
        }catch (Exception e){
            workedId = NetUtil.getLocalhostStr().hashCode();
            e.printStackTrace();
        }
    }

    public synchronized long snowflakeId(){
        return snowflake.nextId();
    }

    /**
        自定义workId和dataId
     */
    public synchronized long snowflakeId(long workedId,long datacenterId){
        Snowflake snowflake = IdUtil.createSnowflake(workedId,datacenterId);
        return snowflake.nextId();
    }
    public static void main(String[] args) {
        IdGeneratorSnowflake idGeneratorSnowflake = new IdGeneratorSnowflake();
        long l = idGeneratorSnowflake.snowflakeId();
        System.out.println("l = " + l);
    }
}
