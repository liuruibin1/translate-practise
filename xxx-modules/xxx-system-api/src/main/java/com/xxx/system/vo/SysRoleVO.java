package com.xxx.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.system.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysRoleVO extends SysRole {

    // 扩展字段 - 外部

    @JsonProperty(access = READ_ONLY)
    List<Integer> permissionIdList;

    // 查询条件字段 - 外部

    @JsonProperty(access = WRITE_ONLY)
    private String key;

    @JsonProperty(access = WRITE_ONLY)
    private Boolean joinPermissionIdList;

}