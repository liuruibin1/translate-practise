package com.xxx.framework.id.utils;

public final class SnowflakeIdUtil {

    private final SnowflakeIdGenerator generator;

    public SnowflakeIdUtil() {
        generator = new SnowflakeIdGenerator(1, 1);
    }

    public long generate() {
        return generator.generateId();
    }

}