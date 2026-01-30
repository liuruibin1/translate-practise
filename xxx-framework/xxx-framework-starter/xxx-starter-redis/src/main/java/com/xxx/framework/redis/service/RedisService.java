package com.xxx.framework.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获得缓存对象
     *
     * @param cacheKey 缓存键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <V> V opsForValueAndGet(final String cacheKey) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return (V) operation.get(buildCacheKey(cacheKey));
    }

    /**
     * 获取数据
     * @param cacheKey 缓存键
     * @param clazz 目标类型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <V> V opsForValueAndGet(final String cacheKey, Class<V> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(buildCacheKey(cacheKey));

            if (value == null) {
                log.debug("⚠️ Redis 键不存在 - Key: {}", buildCacheKey(cacheKey));
                return null;
            }

            // 类型检查
            if (!clazz.isInstance(value)) {
                log.warn("⚠️ Redis 值类型不匹配 - Key: {}, Expected: {}, Actual: {}", buildCacheKey(cacheKey), clazz.getName(), value.getClass().getName());
            }

            log.debug("✅ Redis 读取成功 - Key: {}", buildCacheKey(cacheKey));
            return (V) value;
        } catch (Exception e) {
            log.error("❌ Redis 读取异常 - Key: {}", buildCacheKey(cacheKey), e);
            return null;
        }
    }

    public Long opsForValueAndIncrement(final String cacheKey) {
        return redisTemplate.opsForValue().increment(buildCacheKey(cacheKey));
    }

    public Long opsForValueAndDecrement(final String cacheKey) {
        return redisTemplate.opsForValue().decrement(buildCacheKey(cacheKey));
    }

    //public Double opsForValueAndIncrement(final String cacheKey, final BigDecimal amount) {
    //    return redisTemplate.opsForValue().increment(buildCacheKey(cacheKey), amount.doubleValue());
    //}

    public <V> boolean opsForValueAndSet(final String cacheKey, final V value) {
        try {
            redisTemplate.opsForValue().set(buildCacheKey(cacheKey), value);
            // 验证存储是否成功
            if (!hasKey(cacheKey)) {
                log.error("❌ Redis 存储验证失败 - Key: {}", buildCacheKey(cacheKey));
                return false;
            }
            // 验证过期时间（应该是 -1，表示永不过期）
            Long ttl = getExpire(cacheKey);
            log.debug("✅ Redis 存储成功 - Key: {}, TTL: {} 秒", buildCacheKey(cacheKey), ttl);
            return true;
        } catch (Exception e) {
            log.error("❌ Redis 存储异常 - Key: {}, Value: {}", buildCacheKey(cacheKey), value, e);
            return false;
        }
    }

    /**
     * 缓存的对象，Integer、String、实体类等
     *
     * @param cacheKey 缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param unit 时间颗粒度
     */
    public <V> boolean opsForValueAndSet(final String cacheKey, final V value, final long timeout, final TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(buildCacheKey(cacheKey), value, timeout, unit);

            if (!hasKey(cacheKey)) {
                log.error("❌ Redis 存储验证失败 - Key: {}", buildCacheKey(cacheKey));
                return false;
            }

            Long ttl = redisTemplate.getExpire(buildCacheKey(cacheKey), unit);
            log.debug("✅ Redis 存储成功 - Key: {}, TTL: {} 秒", buildCacheKey(cacheKey), ttl);

            return true;
        } catch (Exception e) {
            log.error("❌ Redis 存储异常 - Key: {}, Timeout: {}", buildCacheKey(cacheKey), timeout, e);
            return false;
        }
    }

    /**
     * 移除对象
     *
     * @param cacheKey  缓存的键值
     */
    public boolean delete(final String cacheKey) {
        //return redisTemplate.delete(cacheKey);
        try {
            Boolean result = redisTemplate.delete(buildCacheKey(cacheKey));
            log.debug("✅ Redis 删除 - Key: {}, Result: {}", buildCacheKey(cacheKey), result);
            return result;
        } catch (Exception e) {
            log.error("❌ Redis 删除异常 - Key: {}", buildCacheKey(cacheKey), e);
            return false;
        }
    }

    /**
     * 获得缓存的set
     *
     * @param cacheKey  缓存的键值
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> opsForSetAndMembers(final String cacheKey) {
        return (Set<T>) redisTemplate.opsForSet().members(buildCacheKey(cacheKey));
    }

    public <T> void opsForSetAndAdd(final String cacheKey, final T value) {
        redisTemplate.opsForSet().add(buildCacheKey(cacheKey), value); // 添加数据
    }

    public <T> void opsForSetAndAddList(final String cacheKey, final Set<T> list) {
        redisTemplate.opsForSet().add(buildCacheKey(cacheKey), list.toArray(new Object[0])); // 添加数据
    }

    /**
     * 缓存的对象，Integer、String、实体类等
     *
     * @param cacheKey 缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void opsForSetAndAddAndExpire(final String cacheKey, final T value, final Long timeout, final TimeUnit timeUnit) {
        opsForSetAndAdd(cacheKey, value); // 添加数据
        expire(cacheKey, timeout, timeUnit); // 设置过期时间
    }

    public void opsForSetAndRemove(String cacheKey, Object value) {
        redisTemplate.opsForSet().remove(buildCacheKey(cacheKey), value);
    }

    public <T> void opsForSetAndRemoveList(String cacheKey, Set<T> values) {
        redisTemplate.opsForSet().remove(buildCacheKey(cacheKey), values.toArray());
    }

    public Long opsForSetAndSize(String cacheKey) {
        return redisTemplate.opsForSet().size(buildCacheKey(cacheKey));
    }

    //@SuppressWarnings("unchecked")
    //public <K, T> Map<K, T> opsForHashAndEntries(final String cacheKey) {
    //    return (Map<K, T>) redisTemplate.opsForHash().entries(buildCacheKey(cacheKey));
    //}

    public <T> void opsForHashAndPut(final String cacheKey, T key, final T value) {
        redisTemplate.opsForHash().put(buildCacheKey(cacheKey), key, value);
    }

    public <T> void opsForHashAndPutAndExpire(final String cacheKey, T key, final T value, final Long timeout, final TimeUnit timeUnit) {
        opsForHashAndPut(cacheKey, key, value);
        expire(cacheKey, timeout, timeUnit); // 设置过期时间
    }

    /**
     * 获取有效时间
     *
     * @param cacheKey Redis键
     * @return 有效时间
     */
    private long getExpire(final String cacheKey) {
        //return redisTemplate.getExpire(cacheKey);
        try {
            return redisTemplate.getExpire(buildCacheKey(cacheKey));
        } catch (Exception e) {
            log.error("❌ Redis getExpire 异常 - Key: {}", buildCacheKey(cacheKey), e);
            return -2L;
        }
    }

    /**
     * 判断 key是否存在
     *
     * @param cacheKey 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String cacheKey) {
        return redisTemplate.hasKey(buildCacheKey(cacheKey));
    }

    /**
     * 获得缓存的对象列表
     *
     * @param cacheKeyPrefix 缓存键值前缀
     * @return 对象列表
     */
    public Set<String> scan100(final String cacheKeyPrefix) {
        return scan(cacheKeyPrefix, 100L);
    }

    ///**
    // * 获得缓存的对象列表
    // *
    // * @param cacheKeyPrefix 缓存键值前缀
    // * @return 对象列表
    // */
    //public Set<String> scanAll(final String cacheKeyPrefix) {
    //    return scan(buildCacheKey(cacheKeyPrefix), null);
    //}

    public Set<Long> sScanLongMembers(String cacheKey, long count) {
        Set<Long> result = new HashSet<>();
        Cursor<Object> cursor = null;
        try {
            ScanOptions options = ScanOptions.scanOptions()
                    .count(count)
                    .build();
            cursor = redisTemplate.opsForSet().scan(buildCacheKey(cacheKey), options);
            while (cursor.hasNext() && result.size() < count) {
                Object v = cursor.next();
                if (v == null) continue;

                if (v instanceof Long) {
                    result.add((Long) v);
                } else {
                    result.add(Long.valueOf(v.toString()));
                }
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception ignored) {
                }
            }
        }
        return result;
    }

    public Set<String> sScanStringMembers(String cacheKey, long count) {
        Set<String> result = new HashSet<>();
        Cursor<Object> cursor = null;
        try {
            ScanOptions options = ScanOptions.scanOptions()
                    .count(count)
                    .build();
            cursor = redisTemplate.opsForSet().scan(buildCacheKey(cacheKey), options);
            while (cursor.hasNext() && result.size() < count) {
                Object v = cursor.next();
                if (v == null) {
                    continue;
                }
                if (v instanceof String) {
                    result.add((String) v);
                } else if (v instanceof byte[]) {
                    // 兼容 JdkSerialization / ByteArray serializer 场景（最保守写法）
                    result.add(new String((byte[]) v, java.nio.charset.StandardCharsets.UTF_8));
                } else {
                    // 兜底：Long/Integer/其他类型
                    result.add(v.toString());
                }
            }
        } catch (Exception e) {
            log.error("❌ Redis SSCAN(Set members) 失败, key={}", buildCacheKey(cacheKey), e);
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception ignored) {
                }
            }
        }
        return result;
    }

    ///**
    // * 批量获取数据（通过前缀扫描）
    // * @param cacheKeyPrefix 键前缀
    // * @param clazz 目标类型
    // * @param count 扫描数量
    // * @return 键值对映射
    // */
    //public <V> java.util.Map<String, V> scanAndOpsForValueAndGet(final String cacheKeyPrefix, Class<V> clazz, Long count) {
    //    java.util.Map<String, V> result = new java.util.HashMap<>();
    //    try {
    //        Set<String> keys = scan(buildCacheKey(cacheKeyPrefix), count);
    //        for (String key : keys) {
    //            V value = opsForValueAndGet(key, clazz);
    //            if (value != null) {
    //                result.put(key, value);
    //            }
    //        }
    //        log.info("✅ 批量获取完成 - 前缀: {}, 获取到 {} 条数据", buildCacheKey(cacheKeyPrefix), result.size());
    //        return result;
    //    } catch (Exception e) {
    //        log.error("❌ 批量获取异常 - 前缀: {}", buildCacheKey(cacheKeyPrefix), e);
    //        return result;
    //    }
    //}

    ///**
    // * 设置key以保存字符串值，如果没有key则设置过期超时。
    // * 并将给定消息发布到给定通道
    // *
    // * @param cacheKey     缓存key
    // * @param timeout 时间值
    // * @param unit    时间单位
    // * @param channel 通道
    // * @param message 消息
    // */
    //public void opsForValueAndSetIfAbsentAndConvertAndSend(String cacheKey, long timeout, TimeUnit unit, String channel, Object message) {
    //    Boolean isNew = redisTemplate.opsForValue().setIfAbsent(buildCacheKey(cacheKey), "sent", timeout, unit);
    //    if (Boolean.TRUE.equals(isNew)) {
    //        redisTemplate.convertAndSend(channel, message);
    //    }
    //}

    /**
     * 设置有效时间
     *
     * @param cacheKey      Redis键
     * @param timeout       超时时间
     * @param unit          时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String cacheKey, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(buildCacheKey(cacheKey), timeout, unit);
    }

    /**
     * 获得缓存的对象列表
     *
     * @param cacheKeyPrefix 缓存键值前缀
     * @param count 个数
     * @return 对象列表
     */
    private Set<String> scan(final String cacheKeyPrefix, Long count) {
        Set<String> keys = new HashSet<>();
        Cursor<String> cursor = null;
        try {
            ScanOptions.ScanOptionsBuilder scanOptionsBuilder = ScanOptions.scanOptions();
            scanOptionsBuilder.match(buildCacheKey(cacheKeyPrefix) + "*");
            if (count != null && count > 0) {
                scanOptionsBuilder.count(count);
            } else {
                scanOptionsBuilder.count(100); // 默认值
            }
            cursor = redisTemplate.scan(scanOptionsBuilder.build());
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
            log.debug("✅ Redis SCAN 完成 - 前缀: {}, 找到 {} 个键", buildCacheKey(cacheKeyPrefix), keys.size());
            return keys;
        } catch (Exception e) {
            log.error("❌ Redis SCAN 异常 - 前缀: {}", buildCacheKey(cacheKeyPrefix), e);
            return keys;
        } finally {
            // 确保关闭 cursor
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    log.warn("⚠️ 关闭 Redis Cursor 失败", e);
                }
            }
        }
    }

    private String buildCacheKey(String cacheKey) {
        if (springProfilesActive != null) {
            return springProfilesActive + ":" + cacheKey;
        } else {
            return cacheKey;
        }
    }

    //public static void main(String[] args) {
    //
    //    // 1️⃣ Redis 配置
    //    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration("150.109.120.130", 6379);
    //    redisConfig.setPassword("foobared"); // 没密码可删除
    //
    //    // 2️⃣ 创建连接工厂
    //    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
    //    connectionFactory.afterPropertiesSet();
    //
    //    // 3️⃣ 创建 RedisTemplate
    //    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    //    redisTemplate.setConnectionFactory(connectionFactory);
    //
    //    // 序列化（非常重要，不然 redis 里是乱码）
    //    redisTemplate.setKeySerializer(new StringRedisSerializer());
    //    redisTemplate.setValueSerializer(new StringRedisSerializer());
    //    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    //    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    //    redisTemplate.afterPropertiesSet();
    //
    //    // 4️⃣ 批量准备数据
    //    String redisKey = "demo:set:users";
    //
    //    Set<String> values = new HashSet<>();
    //    for (int i = 100001; i <= 129107; i++) {
    //        values.add("user_" + i);
    //    }
    //
    //    // 5️⃣ 批量写入 Set（核心代码）
    //    Long addedCount = redisTemplate
    //            .opsForSet()
    //            .add(redisKey, values.toArray(new String[0]));
    //
    //    System.out.println("成功新增数量: " + addedCount);
    //
    //    // 6️⃣ 关闭连接
    //    connectionFactory.destroy();
    //}

}