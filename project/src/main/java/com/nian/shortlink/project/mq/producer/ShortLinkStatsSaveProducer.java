package com.nian.shortlink.project.mq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.nian.shortlink.project.common.constat.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

/**
 * 短链接监控统计消息生产者
 */
@Component
@RequiredArgsConstructor
public class ShortLinkStatsSaveProducer {

    private final StringRedisTemplate stringRedisTemplate;

    public void send(Map<String,String> producerMap){
        stringRedisTemplate.opsForStream().add(SHORT_LINK_STATS_STREAM_TOPIC_KEY,producerMap);
    }
}
