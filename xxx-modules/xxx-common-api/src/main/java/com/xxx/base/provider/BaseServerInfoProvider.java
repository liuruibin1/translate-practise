package com.xxx.base.provider;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseServerInfoProvider {

    public abstract String getSpringApplicationName();

    public Map<String, Object> serverInfo() {
        InetAddress localhost;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ip", localhost.getHostAddress());
        map.put("appName", getSpringApplicationName());
        map.put("gitCommitId", getGitCommitId());
        return map;
    }

    private String getGitCommitId() {
        try (InputStream input = this.getClass().getResourceAsStream("/git.properties")) {
            Properties props = new Properties();
            props.load(input);
            return props.getProperty("git.commit.id"); // 返回如 "a1b2c3d"
        } catch (Exception e) {
            return "unknown";
        }
    }

}
