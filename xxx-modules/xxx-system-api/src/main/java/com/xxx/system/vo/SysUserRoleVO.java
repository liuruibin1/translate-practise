package com.xxx.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.system.entity.SysUserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserRoleVO extends SysUserRole {

    // 查询条件字段 - 外部

    @JsonProperty(access = WRITE_ONLY)
    private String key;

}