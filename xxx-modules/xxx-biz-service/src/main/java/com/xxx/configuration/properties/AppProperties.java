package com.xxx.configuration.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private List<IdSegment> userIdSegmentList;

    @Setter
    @Getter
    public static class IdSegment {
        private Long start;
        private Long end;

    }

}
