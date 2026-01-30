package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum EventEnum {

    TODAY_USER_PROFIT_WARNING(1, "今日用户止盈"),
    TRANSACTIONS_FIAT_RECHARGE(2, "Fiat Top-Up"),
    TRANSACTIONS_FIAT_WITHDRAW(3, "Fiat Withdrawal"),
    TRANSACTIONS_CRYPTO_RECHARGE(4, "Crypto Top-Up"),
    TRANSACTIONS_CRYPTO_WITHDRAW(5, "Crypto Withdrawal"),
    //USER_LOTTERY_ORDER_ESTIMATED_WIN_AMOUNT_WARNING(2, "注单预估能赢告警"),

    //TODAY_LOTTERY_LOSS_WARNING(11, "今日彩票亏损告警"),
    //LOTTERY_ISSUE_DRAW_WARNING(21, "彩期未开奖告警"),
    //LOTTERY_ISSUE_NUMBER_BET_AMOUNT_WARNING(22, "彩期定位胆投注额告警"),

    DASHBOARD(81, "看板统计数据推送"),


    //LOTTERY_ODDS_BE_MODIFIED(91, "彩票赔率被修改"),

    ;

    private final Integer id;
    private final String description;

    EventEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static EventEnum getByValue(Integer value) {
        for (EventEnum enumerate : EventEnum.values()) {
            if (enumerate.getId().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}