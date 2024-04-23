package com.nian.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "rBloomFilterConfigurationByAdmin")
public class RBloomFilterConfiguration {

    @Bean
    public RBloomFilter<String> userRegisterCacheBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cacheBloomFilter = redissonClient.getBloomFilter("userRegisterCacheBloomFilter");
        cacheBloomFilter.tryInit(100000000L, 0.001);
        return cacheBloomFilter;
    }
}