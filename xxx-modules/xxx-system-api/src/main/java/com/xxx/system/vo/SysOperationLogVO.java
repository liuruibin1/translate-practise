package com.xxx.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.system.entity.SysOperationLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysOperationLogVO extends SysOperationLog {

    // 查询条件字段 - 外部

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String key;

}