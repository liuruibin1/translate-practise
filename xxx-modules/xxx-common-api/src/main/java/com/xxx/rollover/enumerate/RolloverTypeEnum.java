package com.xxx.rollover.enumerate;

import lombok.Getter;

@Getter
public enum RolloverTypeEnum {

    DEPOSIT(1, "充值"),
    DEPOSIT_BONUS(2, "充值奖金"),

    BET_REBATE(11, "投注返水"),
    BET_COMMISSION(12, "投注佣金"),

    PLAY_GAME_REWARDS(51, "玩游戏奖励"),
    ;

    private final Integer type;
    private final String description;

    RolloverTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static RolloverTypeEnum getByType(Integer type) {
        for (RolloverTypeEnum enumerate : RolloverTypeEnum.values()) {
            if (enumerate.getType().equals(type)) {
                return enumerate;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (RolloverTypeEnum e : RolloverTypeEnum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.getType());
        }
        System.out.println();
        for (RolloverTypeEnum e : RolloverTypeEnum.values()) { //后端用
            System.out.printf("[RolloverTypeEnum.%s]: '%s',\n", e.name(), e.getDescription());
        }
        System.out.println();
        for (RolloverTypeEnum e : RolloverTypeEnum.values()) { //前端用 国际化
            System.out.printf("[RolloverTypeEnum.%s]: '%s.%s',\n", e.name(), "rollover_type_enum", e.name().toLowerCase());
        }
        System.out.println();
        for (RolloverTypeEnum e : RolloverTypeEnum.values()) { //前端用 国际化
            System.out.printf("\"%s.%s\": \"\",\n", "rollover_type_enum", e.name().toLowerCase());
        }
    }

}