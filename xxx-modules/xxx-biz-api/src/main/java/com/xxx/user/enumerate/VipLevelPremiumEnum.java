package com.xxx.user.enumerate;

import lombok.Getter;

@Getter
public enum VipLevelPremiumEnum {

    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold"),
    PLATINUM_I("Platinum I"),
    PLATINUM_II("Platinum II"),
    DIAMOND_I("Diamond I"),
    DIAMOND_II("Diamond II"),
    DIAMOND_III("Diamond III"),
    ;

    private final String name;

    VipLevelPremiumEnum(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        for (VipLevelPremiumEnum e : VipLevelPremiumEnum.values()) {
            System.out.printf("%s = '%s',\n", e.name(), e.getName());
        }
        for (VipLevelPremiumEnum e : VipLevelPremiumEnum.values()) {
            System.out.printf("[VipLevelPremiumEnum.%s]: '%s',\n", e.name(), e.getName());
        }
    }
    
}