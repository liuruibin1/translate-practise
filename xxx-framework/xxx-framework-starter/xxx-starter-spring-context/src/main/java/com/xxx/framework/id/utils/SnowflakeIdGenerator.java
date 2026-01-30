package com.xxx.framework.id.utils;

public class SnowflakeIdGenerator {
    // ==============================Fields===========================================
    /** 开始时间戳 (可以根据实际需要调整) */
    private static final long START_TIMESTAMP = 1640995200000L; // 2022-01-01 00:00:00
    /** 每一部分的位数 */
    private static final long SEQUENCE_BITS = 8; // 序列号的位数
    private static final long MACHINE_BITS = 2; // 机器ID的位数
    private static final long DATA_CENTER_BITS = 2; // 数据中心ID的位数

    /** 每一部分的最大值 */
    private static final long MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1; // 2^12-1 = 4095
    private static final long MAX_MACHINE_ID = (1 << MACHINE_BITS) - 1; // 2^5-1 = 31
    private static final long MAX_DATA_CENTER_ID = (1 << DATA_CENTER_BITS) - 1; // 2^5-1 = 31

    /** 每一部分向左的位移 */
    private static final long MACHINE_SHIFT = SEQUENCE_BITS; // 12
    private static final long DATA_CENTER_SHIFT = SEQUENCE_BITS + MACHINE_BITS; // 12 + 5 = 17
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_BITS + DATA_CENTER_BITS; // 12 + 5 + 5 = 22

    /** 数据中心ID(0~31) */
    private final long dataCenterId;
    /** 机器ID(0~31) */
    private final long machineId;
    /** 序列号(0~4095) */
    private long sequence = 0L;
    /** 上次生成ID的时间戳 */
    private long lastTimestamp = -1L;

    // ==============================Constructors=====================================
    public SnowflakeIdGenerator(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("DataCenter ID can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException(String.format("Machine ID can't be greater than %d or less than 0", MAX_MACHINE_ID));
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    // ==============================Methods==========================================

    /** 生成唯一的ID (线程安全) */
    public synchronized long generateId() {
        long currentTimestamp = getCurrentTimestamp();

        // 如果当前时间小于上次生成ID的时间，说明系统时钟被回拨，抛出异常
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        // 如果当前时间与上次时间相同，则在同一毫秒内生成序列号
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 如果序列号溢出(0~4095)，则等待下一毫秒
            if (sequence == 0L) {
                currentTimestamp = waitForNextMillis(lastTimestamp);
            }
        } else {
            // 如果是新的毫秒，则重置序列号
            sequence = 0L;
        }

        // 记录生成ID的时间戳
        lastTimestamp = currentTimestamp;

        // 生成ID (通过位运算拼接各部分)
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT) // 时间戳部分
                | (dataCenterId << DATA_CENTER_SHIFT)                     // 数据中心部分
                | (machineId << MACHINE_SHIFT)                            // 机器ID部分
                | sequence;                                               // 序列号部分
    }

    /** 等待直到下一毫秒 */
    private long waitForNextMillis(long lastTimestamp) {
        long currentTimestamp = getCurrentTimestamp();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }

    /** 获取当前时间戳 (毫秒) */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    // ==============================Test=============================================
    public static void main(String[] args) {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);
        Runnable task = () -> {
            for (int i = 0; i < 10; i++) {
                long id = generator.generateId();
                System.out.println(Thread.currentThread().getName() + " generated ID: " + id);
            }
        };
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        t1.start();
        t2.start();
        t3.start();
    }
}

//js
//9007199254740991
//382988286870784
//1866833917361827841
//1734497539108000
//392178071513468928
//392178115067121664
