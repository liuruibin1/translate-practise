package com.xxx.common.core.utils.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xiezm
 */
public class JSONUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * json格式字符 转 集合
     *
     * @param jsonString json格式字符
     * @param valueType  集合内对象的类型
     * @return JSON 字符
     */
    public static <T> T stringToList(String jsonString, Class<?>... valueType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, valueType);
        try {
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * json格式字符 转 集合
     *
     * @param jsonString json格式字符
     * @param valueType  集合内对象的类型
     * @return JSON 字符
     */
    public static <T> T stringToSet(String jsonString, Class<?>... valueType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Set.class, valueType);
        try {
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Entity/vo/pojo 等 bean对象 转 JSON 字符
     *
     * @param object 实体/VO/POJO 等bean对象
     * @return JSON 字符
     */
    public static String objectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Entity/vo/pojo 等 bean的 数组对象 转 JSON 字符
     *
     * @param objectArray 实体/VO/POJO 等bean的数组对象
     * @return JSON 字符
     */
    public static String arrayToString(Object[] objectArray) {
        StringBuilder jsonSB = new StringBuilder();
        jsonSB.append('[');
        for (int i = 0; i < objectArray.length; i++) {
            jsonSB.append(objectToString(objectArray[i]));
            if (i < objectArray.length - 1) {
                jsonSB.append(',');
            }
        }
        jsonSB.append(']');
        return jsonSB.toString();
    }

    /**
     * Entity/vo/pojo 等bean的 集合对象 转 JSON 字符
     *
     * @param list 实体/VO/POJO 等bean的集合对象
     * @return JSON 字符
     */
    public static String listToString(List<?> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Map对象 转 JSON 字符
     *
     * @param map Map对象
     * @return JSON 字符
     */
    public static String mapToString(Map<?, ?> map) {
        return objectToString(map);
    }

    /**
     * json格式字符 转 对象
     *
     * @param jsonString json格式字符
     * @return JSON 字符
     */
    public static <T> T stringToObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 序列化对象 转Java Bean
     *
     * @param object      序列化对象
     * @param toValueType Bean 的 Class
     */
    public static <T> T objectToObject(Object object, Class<T> toValueType) {
        return objectMapper.convertValue(object, toValueType);
    }

    /**
     * 集合序列化对象 转 List
     *
     * @param object    集合序列化对象
     * @param valueType 集合里 Bean 的 Class
     */
    public static <T> T objectToList(Object object, Class<?> valueType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, valueType);
        return objectMapper.convertValue(object, javaType);
    }

}
