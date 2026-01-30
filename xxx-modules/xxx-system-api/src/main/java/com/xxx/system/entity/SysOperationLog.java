package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * SysOperationLog
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysOperationLog implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Long id;

    /**
     * entityId
     * 非空字段
     */
    private Integer entityId;

    /**
     * actionType
     * 非空字段
     */
    private Integer actionType;

    /**
     * operatorType
     * 非空字段
     */
    private Integer operatorType;

    /**
     * operatorId
     * 非空字段
     */
    private Long operatorId;

    /**
     * createTs
     */
    private Date createTs;

    /**
     * data
     * 最大长度: 65535
     */
    private String data;

    private static final long serialVersionUID = 1L;
}