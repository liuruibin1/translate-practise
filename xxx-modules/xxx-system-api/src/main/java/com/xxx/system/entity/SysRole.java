package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * SysRole
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysRole implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Integer id;

    /**
     * name
     * 最大长度: 30
     * 非空字段
     */
    private String name;

    /**
     * sort
     * 非空字段
     */
    private Integer sort;

    /**
     * isEnabled
     * 非空字段
     */
    private Boolean isEnabled;

    /**
     * updateTs
     * 非空字段
     */
    private Date updateTs;

    private static final long serialVersionUID = 1L;
}