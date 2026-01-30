package com.xxx.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.user.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserVO extends User {

    // 扩展字段 - 外部

    @JsonProperty(access = READ_ONLY)
    private String chainName;

    @JsonProperty(access = READ_ONLY)
    private String chainType;

    @JsonProperty(access = READ_ONLY)
    private Boolean chainIsProduction;

    @JsonProperty(access = READ_ONLY)
    private String chainExplorerUrl;


    @JsonProperty(access = READ_ONLY)
    private BigDecimal totalBalanceQV;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal availableBalanceQV;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal frozenBalanceQV;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal withdrawableBalanceQV;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal userGameServiceVendorFundBalanceQV;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal referrerCommissionAmount;


    @JsonProperty(access = READ_ONLY)
    private Integer quoteCurrencyId;

    @JsonProperty(access = READ_ONLY)
    private String quoteCurrencyCode;

    @JsonProperty(access = READ_ONLY)
    private String quoteCurrencySymbol;

    @JsonProperty(access = READ_ONLY)
    private Integer quoteCurrencyType;

    @JsonProperty(access = READ_ONLY)
    private String quoteCurrencyCountryCode;


    @JsonProperty(access = READ_ONLY)
    private Integer settlementCurrencyId;

    @JsonProperty(access = READ_ONLY)
    private String settlementCurrencyCode;

    @JsonProperty(access = READ_ONLY)
    private String settlementCurrencySymbol;

    @JsonProperty(access = READ_ONLY)
    private Integer settlementCurrencyType;

    @JsonProperty(access = READ_ONLY)
    private String settlementCurrencyCountryCode;


    @JsonProperty(access = READ_ONLY)
    private BigDecimal settlementTotalBetAmount;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal settlementReferrerCommissionAmount;


    @JsonProperty(access = READ_ONLY)
    private Long countDirectChildUser;

    @JsonProperty(access = READ_ONLY)
    private List<UserVO> children = new ArrayList<>();


    // 查询条件字段 - 外部

    @JsonProperty(access = WRITE_ONLY)
    private String key;

    @JsonProperty(access = WRITE_ONLY)
    private Long currentMerchantUserId;

    @JsonProperty(access = WRITE_ONLY)
    private Boolean joinSumUserFund;

    @JsonProperty(access = WRITE_ONLY)
    private Integer totalBalanceQVOrderBy;

    @JsonProperty(access = WRITE_ONLY)
    private Integer typeOrderBy;

    @JsonProperty(access = WRITE_ONLY)
    private Integer createTsMsOrderBy;

    @JsonProperty(access = WRITE_ONLY)
    private Boolean joinCountDirectChildUser;

    @JsonProperty(access = WRITE_ONLY)
    private String createTsMsStrGE;

    @JsonProperty(access = WRITE_ONLY)
    private String createTsMsStrLE;

    @JsonProperty(access = WRITE_ONLY)
    private Long createTsMsGE;

    @JsonProperty(access = WRITE_ONLY)
    private Long createTsMsLE;

    // 查询条件字段 - 内部

    @JsonIgnore
    private Integer displayQuoteCurrencyId;

    @JsonIgnore //必须与 joinSumUserFund 配合使用
    private Integer userRolloverStatusCompleted;

    @JsonIgnore
    private String profitOrLoss;

}