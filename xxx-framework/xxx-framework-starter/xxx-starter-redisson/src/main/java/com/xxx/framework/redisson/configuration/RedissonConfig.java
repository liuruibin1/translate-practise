package com.xxx.framework.redisson.configuration;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.SslVerificationMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    private final RedisProperties redisProperties;

    public RedissonConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {

        if (springProfilesActive.startsWith("prod")) { //生产

            boolean sslEnabled = redisProperties.getSsl() != null && redisProperties.getSsl().isEnabled();
            String scheme = sslEnabled ? "rediss://" : "redis://";

            Config config = new Config();
            config.setCodec(new JsonJacksonCodec());

            String username = blankToNull(redisProperties.getUsername());
            String password = blankToNull(redisProperties.getPassword());

            RedisProperties.Cluster clusterProps = redisProperties.getCluster();
            List<String> nodes = (clusterProps != null ? clusterProps.getNodes() : null);

            if (nodes == null || nodes.isEmpty()) {
                // ---------- Single ----------
                String addr = normalizeAddress(scheme, redisProperties.getHost(), redisProperties.getPort());

                var single = config.useSingleServer()
                        .setAddress(addr)
                        .setDatabase(redisProperties.getDatabase())
                        .setIdleConnectionTimeout(10_000)
                        .setConnectTimeout(10_000)
                        .setTimeout(3_000)
                        .setRetryAttempts(3)
                        .setRetryInterval(1_500)
                        .setPingConnectionInterval(30_000)
                        .setKeepAlive(true)
                        .setConnectionPoolSize(64)
                        .setConnectionMinimumIdleSize(10);

                applyAuthAndSsl(single, sslEnabled, username, password);

                log.info("Redisson init (single) success, address={}, ssl={}", addr, sslEnabled);

            } else {
                // ---------- Cluster ----------
                ClusterServersConfig cluster = config.useClusterServers();

                for (String raw : nodes) {
                    String node = blankToNull(raw);
                    if (node == null) continue;

                    // 支持 nodes 里写 host:port / redis://host:port / rediss://host:port / host(无端口)
                    cluster.addNodeAddress(normalizeNode(scheme, node, redisProperties.getPort()));
                }

                applyAuthAndSsl(cluster, sslEnabled, username, password);

                // ⚠️ 如果你是 docker / k8s / 跨网段访问 cluster，经常需要 NAT 映射（按需开启）
                // 示例：把 cluster 返回的 "10.0.1.23:6379" 映射成 "public-host:6379"
                // cluster.setNatMapper(new HostPortNatMapper()
                //         .add("10.0.1.23", "public-host")
                //         .add("10.0.1.24", "public-host2"));

                cluster.setMasterConnectionPoolSize(64)
                        .setMasterConnectionMinimumIdleSize(10)
                        .setIdleConnectionTimeout(10_000)
                        .setConnectTimeout(10_000)
                        .setTimeout(10_000)
                        .setRetryAttempts(1)
                        .setRetryInterval(1_000)
                        .setPingConnectionInterval(30_000)
                        .setKeepAlive(true);

                log.info("Redisson init (cluster) success, nodes={}, ssl={}", nodes, sslEnabled);
            }
            int cpu = Runtime.getRuntime().availableProcessors();

            int netty = Math.min(8, cpu * 2);
            int biz = Math.min(8, cpu * 2);

            config.setNettyThreads(netty);
            config.setThreads(biz);

            return Redisson.create(config);
        } else {
            log.info("初始化 RedissonClient - Host: {}, Port: {}, DB: {}", redisProperties.getHost(), redisProperties.getPort(), redisProperties.getDatabase());

            Config config = new Config();

            // 使用 Jackson 编码器（更稳定）
            config.setCodec(new JsonJacksonCodec());

            // 单机模式配置
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                    .setDatabase(redisProperties.getDatabase())
                    .setConnectionPoolSize(64)           // 连接池大小
                    .setConnectionMinimumIdleSize(10)    // 最小空闲连接数
                    .setIdleConnectionTimeout(10000)     // 空闲连接超时时间(毫秒)
                    .setConnectTimeout(10000)            // 连接超时时间(毫秒)
                    .setTimeout(3000)                    // 命令等待超时时间(毫秒)
                    .setRetryAttempts(3)                 // 命令失败重试次数
                    //.setRetryInterval(1500)              // 命令重试间隔(毫秒)
                    .setPingConnectionInterval(30000)    // ping间隔(毫秒)
                    .setKeepAlive(true);                 // 保持连接

            // 设置密码（如果不是本地环境）
            if (redisProperties.getPassword() != null && !redisProperties.getPassword().isEmpty()
                    && !redisProperties.getHost().equals("127.0.0.1")
                    && !redisProperties.getHost().equals("localhost")) {
                singleServerConfig.setPassword(redisProperties.getPassword());
                log.info("Redis 密码已配置");
            }

            // 线程池配置
            config.setThreads(16);          // 处理线程数
            config.setNettyThreads(32);     // Netty 线程数

            RedissonClient client = Redisson.create(config);
            log.info("RedissonClient 初始化完成");

            return client;
        }
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private static String normalizeAddress(String scheme, String host, int port) {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("redis host is blank");
        }
        // host 里如果意外写了 redis://，也处理掉
        String h = host.trim().replaceFirst("^rediss?://", "");
        return scheme + h + ":" + port;
    }

    private static String normalizeNode(String scheme, String node, Integer defaultPort) {
        String n = node.trim();
        boolean hasScheme = n.startsWith("redis://") || n.startsWith("rediss://");
        if (!hasScheme) {
            // 没 scheme：补 scheme
            n = scheme + n;
        }

        // 没端口：补默认端口
        // 仅做简单判断：最后一个 ':' 后是数字才算带端口；否则补端口
        int lastColon = n.lastIndexOf(':');
        boolean hasPort = lastColon > n.indexOf("://") + 2
                && lastColon < n.length() - 1
                && n.substring(lastColon + 1).chars().allMatch(Character::isDigit);

        if (!hasPort) {
            int p = (defaultPort != null && defaultPort > 0) ? defaultPort : 6379;
            n = n + ":" + p;
        }

        return n;
    }

    private static void applyAuthAndSsl(SingleServerConfig single, boolean sslEnabled, String username, String password) {
        if (sslEnabled) {
            single.setSslVerificationMode(SslVerificationMode.NONE); // 生产建议 STRICT
        }
        if (username != null) single.setUsername(username);
        if (password != null) single.setPassword(password);
    }

    private static void applyAuthAndSsl(ClusterServersConfig cluster, boolean sslEnabled, String username, String password) {
        if (sslEnabled) {
            cluster.setSslVerificationMode(SslVerificationMode.NONE); // 生产建议 STRICT
        }
        if (username != null) cluster.setUsername(username);
        if (password != null) cluster.setPassword(password);
    }
}
