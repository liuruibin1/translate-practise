package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * SysDept
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysDept implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Integer id;

    /**
     * parentId
     * 非空字段
     */
    private Integer parentId;

    /**
     * name
     * 最大长度: 32
     * 非空字段
     */
    private String name;

    /**
     * sort
     * 非空字段
     */
    private Integer sort;

    /**
     * updateTs
     * 非空字段
     */
    private Date updateTs;

    private static final long serialVersionUID = 1L;
}