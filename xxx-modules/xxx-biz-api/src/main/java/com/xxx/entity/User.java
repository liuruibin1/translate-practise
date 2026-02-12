package com.xxx.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {
    /**
     * id
     * 自增字段
     */
    private Long id;

    /**
     * username
     * 最大长度: 64
     * 非空字段
     */
    private String username;

    /**
     * password
     * 最大长度: 1024
     * 非空字段
     * 敏感字段，JSON序列化时忽略
     */
    @JsonIgnore
    private String password;

    /**
     * level
     * 非空字段
     */
    private Integer level;

    /**
     * avgScore
     * 非空字段
     */
    private Integer avgScore;

    /**
     * createTs
     * 非空字段
     */
    private Date createTs;

    private static final long serialVersionUID = 1L;
}