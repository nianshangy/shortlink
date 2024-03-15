package com.nian.shortlink.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBloomFilterConfiguration {

    @Bean
    public RBloomFilter<String> shortLinkCreateCacheBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cacheBloomFilter = redissonClient.getBloomFilter("userRegisterCacheBloomFilter");
        cacheBloomFilter.tryInit(100000000L, 0.001);
        return cacheBloomFilter;
    }
}