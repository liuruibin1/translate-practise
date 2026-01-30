package com.xxx.framework.redis.configuration;

import com.xxx.framework.redis.service.RedisService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类 - 优化版
 */
@Configuration
@EnableCaching
@AutoConfigureBefore({RedisAutoConfiguration.class, RedisService.class})
public class RedisConfig implements CachingConfigurer {

    /**
     * RedisTemplate 配置
     * 使用 FastJson2 作为序列化器，支持类型信息保存
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // 注意：这里建议你明确 fastjson2 的安全策略（白名单等），不要只靠默认
        FastJson2JsonRedisSerializer<Object> valueSerializer = FastJson2JsonRedisSerializer.createObjectSerializer();

        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        // 不要硬编码 false，交给业务/配置
        // template.setEnableTransactionSupport(false);

        template.afterPropertiesSet();
        return template;
    }

    //@Bean
    //public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    //    RedisTemplate<String, String> template = new RedisTemplate<>();
    //    template.setConnectionFactory(connectionFactory);
    //    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    //    template.setKeySerializer(stringRedisSerializer);
    //    template.setValueSerializer(stringRedisSerializer);
    //    template.setHashKeySerializer(stringRedisSerializer);
    //    template.setHashValueSerializer(stringRedisSerializer);
    //    template.afterPropertiesSet();
    //    return template;
    //}

}