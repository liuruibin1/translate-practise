package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum BusinessTypeEnum {

    USER_GAME_ORDER_BET(12, false, "投注扣款(游戏)"),
    USER_GAME_ORDER_PRIZE(13, true, "投注派奖(游戏)"),
    USER_GAME_ORDER_PRIZE_REVOKE(14, false, "撤销派奖(游戏)"),

    USER_GAME_ORDER_BET_CANCEL_OF_SYSTEM(17, true, "撤单退款(系统)(游戏)"),
    USER_GAME_ORDER_DRAW_REFUND(18, true, "平局返本(游戏)"),

    USER_TRANSFER_IN(21, true, "用户转入"),
    USER_TRANSFER_OUT(22, false, "用户转出"),

    USER_CRYPTO_DEPOSIT(23, true, "用户充值(加密)"),
    USER_FIAT_DEPOSIT(25, true, "用户充值(法定)"),
    USER_CRYPTO_WITHDRAW(26, false, "用户提现(加密)"),
    USER_FIAT_WITHDRAW(27, false, "用户提现(法定)"),

    USER_DAILY_BET_REBATE(28, true, "用户每日投注返水"),
    USER_DAILY_BET_COMMISSION(29, true, "用户每日投注返佣"),

    USER_FUND_INCREASE(31, true, "用户加款"),
    USER_FUND_DECREASE(32, false, "用户扣款"),

    USER_UPGRADE_COMMISSION(33, true, "用户升级佣金"),

    USER_FUND_FREEZE(36, false, "用户资金冻结"),
    USER_FUND_UNFREEZE(37, true, "用户资金解冻"),
    USER_FUND_FREEZE_ERASE(38, null, "用户资金冻结擦除"),

    USER_ROLLOVER(51, null, "用户流水要求"),
    USER_BONUS(52, true, "用户奖金"),

    //DAILY_COMPETITIONS(53, true, "每日竞赛"),
    //WEEK_RAFFLE(54, true, "每周抽奖"),
    //PROMOTION_EVENT(55, true, "促销活动"),

    USER_PLAY_GAME_REWARDS(61, true, "用户玩游戏奖励"),
    USER_ITEM_ORDER(62, false, "用户道具订单"),
    USER_TASK_REWARDS(63, true, "用户任务奖励"),

    VERIFICATION_CODE(101, null, "验证码"),
    EVENT(102, null, "事件"),

    NOTIFICATION(121, null, "通知"),

    USER_GAME_ORDER_NEW_GAMING(150, null, "用户游戏订单(NewGaming)"), //admin 后台管理使用
    USER_GAME_ORDER_QUICK_GAMING(151, null, "用户游戏订单(QuickGaming)"), //admin 后台管理使用

    STATS_USER_DAILY_BET(161, null, "统计用户每天投注"),  //admin 后台管理使用
    ;

    private final Integer type;
    private final Boolean increaseOrDecrease;
    private final String description;

    BusinessTypeEnum(Integer type, Boolean increaseOrDecrease, String description) {
        this.type = type;
        this.increaseOrDecrease = increaseOrDecrease;
        this.description = description;
    }

    public static BusinessTypeEnum getByType(Integer type) {
        for (BusinessTypeEnum enumerate : BusinessTypeEnum.values()) {
            if (enumerate.getType().equals(type)) {
                return enumerate;
            }
        }
        return null;
    }

    public static void main(String[] args) {

        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.getType());
        }
        System.out.println();
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            System.out.printf("[BusinessTypeEnum.%s]: {\n", e.name());
            System.out.printf("  id: BusinessTypeEnum.%s,\n", e.name());
            System.out.printf("  increaseOrDecrease: %s,\n", e.getIncreaseOrDecrease());
            System.out.printf("  label: '%s',\n", e.getDescription());
            System.out.print("},\n");
        }
        System.out.println();
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) { //前端用 国际化
            System.out.printf("[BusinessTypeEnum.%s]: '%s.%s',\n", e.name(), "business_type_enum", e.name().toLowerCase());
        }
        System.out.println();
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) { //前端用 国际化
            System.out.printf("\"%s.%s\": \"\",\n", "business_type_enum", e.name().toLowerCase());
        }

    }

}