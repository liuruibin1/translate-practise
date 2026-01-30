package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;

/**
 * 用户角色表
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysUserRole implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Integer id;

    /**
     * 系统用户ID
     */
    private Long sysUserId;

    /**
     * roleId
     * 非空字段
     */
    private Integer roleId;

    private static final long serialVersionUID = 1L;
}