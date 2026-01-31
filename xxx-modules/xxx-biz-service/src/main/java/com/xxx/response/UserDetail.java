package com.xxx.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetail implements Serializable {

    //实体字段
    private Long id;

    private Integer type2;

    private Long telegramId;

    private String email;

    private String phoneNumber;

    private String username;

    private String avatarUrl;

    private String referralCode;

    private Integer vipLevel;

    private Long createTsMs;

}