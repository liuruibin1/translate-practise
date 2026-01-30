package com.xxx.framework.redis.configuration;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis 使用 FastJson2 序列化
 * 优化版：支持类型信息保存，解决反序列化类型丢失问题
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Class<T> clazz;

    /**
     * FastJson2 序列化特性
     * WriteClassName: 写入类型信息，解决反序列化类型问题
     * WriteMapNullValue: 写入 null 值
     * WriteDateUseDateFormat: 日期格式化
     */
    private static final JSONWriter.Feature[] WRITER_FEATURES = {
            JSONWriter.Feature.WriteClassName,
            JSONWriter.Feature.WriteMapNullValue
            //JSONWriter.Feature.WriteDateUseDateFormat
    };

    /**
     * FastJson2 反序列化特性
     * SupportAutoType: 支持自动类型识别（安全模式）
     * SupportArrayToBean: 支持数组转对象
     */
    private static final JSONReader.Feature[] READER_FEATURES = {
            JSONReader.Feature.SupportAutoType,
            JSONReader.Feature.SupportArrayToBean,
            JSONReader.Feature.FieldBased
    };

    /**
     * 自动类型白名单过滤器（安全配置）
     */
    private static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(
            // 允许的包前缀（根据你的项目调整）
            "com.xxx.",
            "java.lang.",
            "java.util.",
            "java.time."
    );

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONString(t, WRITER_FEATURES).getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            String str = new String(bytes, DEFAULT_CHARSET);

            // 如果 clazz 是 Object.class，使用自动类型识别
            if (clazz == Object.class) {
                return (T) JSON.parseObject(str, Object.class, AUTO_TYPE_FILTER, READER_FEATURES);
            }

            // 否则使用指定类型反序列化
            return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER, READER_FEATURES);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize: " + e.getMessage(), e);
        }
    }

    /**
     * 创建针对 Object 类型的序列化器（支持自动类型识别）
     */
    public static FastJson2JsonRedisSerializer<Object> createObjectSerializer() {
        return new FastJson2JsonRedisSerializer<>(Object.class);
    }

    /**
     * 创建针对特定类型的序列化器
     */
    public static <T> FastJson2JsonRedisSerializer<T> createSerializer(Class<T> clazz) {
        return new FastJson2JsonRedisSerializer<>(clazz);
    }

}