package com.nian.shortlink.aggregation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 聚合服务应用启动器
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "com.nian.shortlink.admin",
        "com.nian.shortlink.project"
})
@MapperScan(value = {
        "com.nian.shortlink.project.mapper",
        "com.nian.shortlink.admin.mapper"
})
public class ShortLinkAggregationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortLinkAggregationApplication.class,args);
    }
}
