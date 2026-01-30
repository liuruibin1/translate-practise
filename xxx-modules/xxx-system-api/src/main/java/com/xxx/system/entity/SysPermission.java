package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * SysPermission
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysPermission implements Serializable {
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
     * type
     * 非空字段
     */
    private Integer type;

    /**
     * name
     * 最大长度: 32
     * 非空字段
     */
    private String name;

    /**
     * code
     * 最大长度: 64
     */
    private String code;

    /**
     * sort
     * 非空字段
     */
    private Integer sort;

    /**
     * urlPath
     * 最大长度: 128
     */
    private String urlPath;

    /**
     * urlQuery
     * 最大长度: 64
     */
    private String urlQuery;

    /**
     * pageComponent
     * 最大长度: 128
     */
    private String pageComponent;

    /**
     * icon
     * 最大长度: 32
     */
    private String icon;

    /**
     * isCache
     * 非空字段
     */
    private Boolean isCache;

    /**
     * isVisible
     * 非空字段
     */
    private Boolean isVisible;

    /**
     * isPageFrame
     * 非空字段
     */
    private Boolean isPageFrame;

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