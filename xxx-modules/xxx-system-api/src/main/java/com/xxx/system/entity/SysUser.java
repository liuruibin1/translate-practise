package com.xxx.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * SysUser
 * 
 * @author MyBatis Generator
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysUser implements Serializable {
    /**
     * id
     * 非空字段
     */
    private Long id;

    /**
     * deptId
     * 非空字段
     */
    private Integer deptId;

    /**
     * username
     * 最大长度: 64
     * 非空字段
     */
    private String username;

    /**
     * 商户用户ID
     */
    private Long merchantUserId;

    /**
     * password
     * 最大长度: 128
     * 非空字段
     */
    private String password;

    /**
     * nickName
     * 最大长度: 64
     * 非空字段
     */
    private String nickName;

    /**
     * email
     * 最大长度: 64
     */
    private String email;

    /**
     * phoneNumber
     * 最大长度: 32
     */
    private String phoneNumber;

    /**
     * telegramId
     */
    private Long telegramId;

    /**
     * isEnabled
     * 非空字段
     */
    private Boolean isEnabled;

    /**
     * isDeleted
     * 非空字段
     */
    private Boolean isDeleted;

    /**
     * remark
     * 最大长度: 128
     */
    private String remark;

    /**
     * createTs
     * 非空字段
     */
    private Date createTs;

    /**
     * updateTs
     * 非空字段
     */
    private Date updateTs;

    /**
     * avatar
     * 最大长度: 65535
     */
    private String avatar;

    private static final long serialVersionUID = 1L;
}