package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;

/**
 * SysRolePermission
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysRolePermission implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Integer id;

    /**
     * roleId
     * 非空字段
     */
    private Integer roleId;

    /**
     * permissionId
     * 非空字段
     */
    private Integer permissionId;

    private static final long serialVersionUID = 1L;
}