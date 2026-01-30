package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum EstimateTypeEnum {

    NO_ESTIMATE_RANDOM(0, "不估算直接随机"),
    ESTIMATE_PROFIT_CLOSEST(1, "估算利润取最接近"),
    ESTIMATE_PROFIT_RANDOM(2, "估算利润取随机"), //投注中
    ESTIMATE_ISSUE_LOOSENING(6, "估算彩期放水(连续)"),
    ESTIMATE_ISSUE_LOOSENING_RANDOM(7, "估算单期放水未匹配后随机"),
    ESTIMATE_ISSUE_LOOSENING_ID_ODD(8, "估算彩期放水(ID单)"),
    ESTIMATE_ISSUE_LOOSENING_ID_EVEN(9, "估算彩期放水(ID双)"),

    ;

    private final Integer value;
    private final String description;

    EstimateTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static EstimateTypeEnum getByValue(Integer id) {
        for (EstimateTypeEnum enumerate : EstimateTypeEnum.values()) {
            if (enumerate.getValue().equals(id)) {
                return enumerate;
            }
        }
        return null;
    }

}