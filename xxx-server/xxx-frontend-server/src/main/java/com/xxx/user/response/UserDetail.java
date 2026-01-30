package com.xxx.user.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetail implements Serializable {

    //实体字段

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "type2")
    private Integer type2;

    @Schema(description = "电报ID")
    private Long telegramId;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "电话号码")
    private String phoneNumber;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "推荐码")
    private String referralCode;

    @Schema(description = "用户VIP等级")
    private Integer vipLevel;

    @Schema(description = "创建时间(毫秒)")
    private Long createTsMs;

    //VO字段


    @Schema(description = "总金额计价")
    private BigDecimal totalBalanceQV;

    @Schema(description = "可用金额计价")
    private BigDecimal availableBalanceQV;

    @Schema(description = "冻结金额计价")
    private BigDecimal frozenBalanceQV;

    @Schema(description = "可提现金额计价")
    private BigDecimal withdrawableBalanceQV;

    @Schema(description = "用户三方金额计价")
    private BigDecimal userGameServiceVendorFundBalanceQV;


    @Schema(description = "推荐佣金总额")
    private BigDecimal referrerCommissionAmount;


    @Schema(description = "用户游戏注单实际返奖额(计价)")
    private BigDecimal userGameOrderActualPrizeAmountQV;

    @Schema(description = "用户游戏注单投注数")
    private Integer userGameOrderBetCount;

    @Schema(description = "用户游戏注单有效投注额(计价)")
    private BigDecimal userGameOrderEffectiveBetAmountQV;


    @Schema(description = "货币ID(计价)")
    private Integer quoteCurrencyId;

    @Schema(description = "货币编码(计价)")
    private String quoteCurrencyCode;

    @Schema(description = "货币符号(计价)")
    private String quoteCurrencySymbol;

    @Schema(description = "货币类型(计价)")
    private Integer quoteCurrencyType;

    @Schema(description = "货币国家简码(计价)")
    private String quoteCurrencyCountryCode;


    @Schema(description = "货币ID(结算)")
    private Integer settlementCurrencyId;

    @Schema(description = "货币编码(结算)")
    private String settlementCurrencyCode;

    @Schema(description = "货币符号(结算)")
    private String settlementCurrencySymbol;

    @Schema(description = "货币类型(结算)")
    private Integer settlementCurrencyType;

    @Schema(description = "货币国家简码(结算)")
    private String settlementCurrencyCountryCode;


    @Schema(description = "积分(总下注)(结算)")
    private BigDecimal settlementTotalBetAmount;

    @Schema(description = "推荐佣金额(结算)")
    private BigDecimal settlementReferrerCommissionAmount;


    @Schema(description = "游戏ID")
    private Integer gameId;

    @Schema(description = "游戏名")
    private String gameName;

    @Schema(description = "游戏名(默认)")
    private String gameDefaultName;

    @Schema(description = "游戏图片URL")
    private String gameImageUrl;

    @Schema(description = "游戏图片URL(默认)")
    private String gameDefaultImageUrl;

    @Schema(description = "游戏URL")
    private String gameUrl;

}