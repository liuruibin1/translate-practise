package com.xxx.mutation.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheVO implements Serializable {

    // 扩展字段 - 外部

    @JsonProperty(access = READ_ONLY)
    private String cacheKey;

    @JsonProperty(access = READ_ONLY)
    private Long expire;

    @JsonProperty(access = READ_ONLY)
    private String dataString;

}