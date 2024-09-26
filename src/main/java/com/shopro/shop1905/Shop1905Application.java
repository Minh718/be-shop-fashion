package com.shopro.shop1905;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import ch.qos.logback.classic.Logger;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableScheduling
public class Shop1905Application {
	public static void main(String[] args) {
		SpringApplication.run(Shop1905Application.class, args);
		//
	}
}