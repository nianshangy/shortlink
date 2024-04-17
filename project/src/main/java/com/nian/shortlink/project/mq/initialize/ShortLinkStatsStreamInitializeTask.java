package com.nian.shortlink.project.mq.initialize;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.nian.shortlink.project.common.constat.RedisKeyConstant.SHORT_LINK_STATS_STREAM_GROUP_KEY;
import static com.nian.shortlink.project.common.constat.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

/**
 * 初始化短链接监控消息队列消费者组
 */
@Component
@RequiredArgsConstructor
public class ShortLinkStatsStreamInitializeTask implements InitializingBean {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 创建stream消息队列与消费者组
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Boolean hasKey = stringRedisTemplate.hasKey(SHORT_LINK_STATS_STREAM_TOPIC_KEY);
        if (hasKey == null || !hasKey) {
            stringRedisTemplate.opsForStream().createGroup(SHORT_LINK_STATS_STREAM_TOPIC_KEY, SHORT_LINK_STATS_STREAM_GROUP_KEY);
        }
    }
}
