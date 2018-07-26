package com.file.main.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //1.@Scheduled(fixedRate = 5000) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间5秒执行一次 
    //2.@Scheduled(cron = “0 0/10 * * * ?”) //使用cron属性可按照指定时间执行，本例指的是每10分钟执行一次；
    @Scheduled(cron = "0 0 2 * * ?") // 每10分钟执行一次
    public void getToken() {
        logger.info("getToken定时任务启动");
    }
}