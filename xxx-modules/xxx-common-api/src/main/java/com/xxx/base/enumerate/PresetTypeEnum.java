package com.xxx.base.enumerate;

import lombok.Getter;

/**
 * 预设方式
 */
@Getter
public enum PresetTypeEnum {

    MANUAL(0, "手工设置"),
    ESTIMATE(1, "投注估算");

    private final Integer value;
    private final String description;

    PresetTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static PresetTypeEnum getByValue(Integer id) {
        for (PresetTypeEnum enumerate : PresetTypeEnum.values()) {
            if (enumerate.getValue().equals(id)) {
                return enumerate;
            }
        }
        return null;
    }

}