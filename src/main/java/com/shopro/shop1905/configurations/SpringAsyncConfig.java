package com.shopro.shop1905.configurations;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

    @Bean("ThreadPoolEmail")
    public Executor taskExecutorEmail() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean("ThreadPoolUserVoucher")
    public Executor ThreadPoolUserVoucher() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean("ThreadPoolLoyalUser")
    public Executor ThreadPoolLoyalUser() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean("rollBackOrder")
    public Executor taskExecutorRollBackOrder() {
        return Executors.newCachedThreadPool();
    }
    // @Bean("rollBackOrder")
    // public Executor taskExecutor() {
    // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // executor.setCorePoolSize(10);
    // executor.setMaxPoolSize(20);
    // executor.setQueueCapacity(50);
    // executor.setThreadNamePrefix("ThreadEmail-");
    // executor.initialize();
    // return executor;
    // }

}
