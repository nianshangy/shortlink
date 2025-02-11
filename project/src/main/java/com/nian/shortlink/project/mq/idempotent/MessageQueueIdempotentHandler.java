package com.nian.shortlink.project.mq.idempotent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 消息队列幂等处理器
 */
@Component
@RequiredArgsConstructor
public class MessageQueueIdempotentHandler {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String IDEMPOTENT_KEY_PREFIX = "short-link:idempotent:";

    /**
     * 判断消息是否被消费过
     *
     * @param messageId 消息唯一标识
     * @return 消息是否消费过
     */
    public boolean isMessageProcessed(String messageId){
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key,"0",10, TimeUnit.MINUTES));
    }

    /**
     * 判断消息消费流程是否完成
     *
     * @param messageId 消息唯一标识
     * @return 消息是否执行完成
     */
    public boolean isAccomplish(String messageId){
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        return Objects.equals("1",stringRedisTemplate.opsForValue().get(key));
    }

    /**
     * 设置消息流程消费完成
     *
     * @param messageId 消息唯一标识
     */
    public void setAccomplish(String messageId) {
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        stringRedisTemplate.opsForValue().set(key,"1",10, TimeUnit.MINUTES);
    }

    /**
     * 删除消息的幂等标识
     *
     * @param messageId 消息唯一标识
     */
    public void deleteMessageProcessed(String messageId){
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        stringRedisTemplate.delete(key);
    }
}
