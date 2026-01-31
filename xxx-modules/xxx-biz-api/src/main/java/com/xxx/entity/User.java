package com.xxx.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

/**
 * Users and global privileges
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
     * 非空字段
     */
    private Long id;

    /**
     * 用户类型
     * 非空字段
     */
    private Integer type;

    /**
     * type2
     * 非空字段
     */
    private Integer type2;

    /**
     * 商户等级
     * 非空字段
     */
    private Integer merchantGrade;

    /**
     * 电报ID
     */
    private Long telegramId;

    /**
     * 链ID
     */
    private Integer chainId;

    /**
     * 链地址
     * 最大长度: 64
     */
    private String chainAddress;

    /**
     * 邮箱地址
     * 最大长度: 128
     */
    private String email;

    /**
     * 推特ID
     * 最大长度: 64
     */
    private String twitterId;

    /**
     * Facebook ID
     * 最大长度: 64
     */
    private String facebookId;

    /**
     * Line ID
     * 最大长度: 64
     */
    private String lineId;

    /**
     * 手机号
     * 最大长度: 32
     */
    private String phoneNumber;

    /**
     * 用户名
     * 最大长度: 64
     * 非空字段
     */
    private String username;

    /**
     * 多个接收消息电报ID
     * 最大长度: 256
     */
    private String receiveMessagesTelegramIds;

    /**
     * 盐值
     * 最大长度: 64
     * 非空字段
     */
    private String salt;

    /**
     * 用户密码
     * 最大长度: 128
     * 敏感字段，JSON序列化时忽略
     */
    @JsonIgnore
    private String password;

    /**
     * 提现密码
     * 最大长度: 8
     */
    private String withdrawalPassword;

    /**
     * APP KEY
     * 最大长度: 32
     */
    private String appKey;

    /**
     * 是否启用
     * 非空字段
     */
    private Boolean isEnabled;

    /**
     * 是否测试用户
     * 非空字段
     */
    private Boolean isTest;

    /**
     * 头像地址
     * 最大长度: 1024
     */
    private String avatarUrl;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 推荐人ID
     */
    private Long referrerId;

    /**
     * 推荐码
     * 最大长度: 32
     * 非空字段
     */
    private String referralCode;

    /**
     * 总投注额
     * 非空字段
     */
    private BigDecimal totalBetAmount;

    /**
     * 当前VIP等级
     * 非空字段
     */
    private Integer vipLevel;

    /**
     * 版本
     * 非空字段
     */
    private Integer version;

    /**
     * 创建时间(毫秒)
     */
    private Long createTsMs;

    private static final long serialVersionUID = 1L;
}