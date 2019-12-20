/*
 * Copyright (c) 2019.
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 * Written by Thomas Schalldach software@schalldach.com
 */

package com.schalldach.dns.blockit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
public class BlockitApplication {

    public static final String ASYNC_WORKER = "Async-Worker-";
    @Value("${async.worker.core.poolsize:20}")
    private Integer corePoolSize;

    @Value("${async.worker.queue.cap:200}")
    private Integer queueCapacity;

    @Value("${async.worker.max.poolsize:100}")
    private Integer maxPoolSize;

    public static void main(String[] args) {
        SpringApplication.run(BlockitApplication.class, args);
    }


    @Bean("asyncWorker")
    public TaskExecutor taskExecutor(@Value("${async.worker.core.poolsize:20}") final int corePoolSize,
                                     @Value("${async.worker.queue.cap:200}") final int queueCapacity,
                                     @Value("${async.worker.max.poolsize:100}") final int maxPoolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadNamePrefix(ASYNC_WORKER);
        return executor;
    }

}
