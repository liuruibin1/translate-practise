package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * SysUserLoginLog
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysUserLoginLog implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Long id;

    /**
     * username
     * 最大长度: 64
     * 非空字段
     */
    private String username;

    /**
     * isSuccessful
     * 非空字段
     */
    private Boolean isSuccessful;

    /**
     * remark
     * 最大长度: 128
     */
    private String remark;

    /**
     * createTs
     */
    private Date createTs;

    private static final long serialVersionUID = 1L;
}