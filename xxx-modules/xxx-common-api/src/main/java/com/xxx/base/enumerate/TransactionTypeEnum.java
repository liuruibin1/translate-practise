package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum TransactionTypeEnum {

    USER_GAME_ORDER_BET(BusinessTypeEnum.USER_GAME_ORDER_BET),
    USER_GAME_ORDER_PRIZE(BusinessTypeEnum.USER_GAME_ORDER_PRIZE),
    USER_GAME_ORDER_PRIZE_REVOKE(BusinessTypeEnum.USER_GAME_ORDER_PRIZE_REVOKE),

    USER_GAME_ORDER_BET_CANCEL_OF_SYSTEM(BusinessTypeEnum.USER_GAME_ORDER_BET_CANCEL_OF_SYSTEM),
    USER_GAME_ORDER_DRAW_REFUND(BusinessTypeEnum.USER_GAME_ORDER_DRAW_REFUND),

    USER_TRANSFER_IN(BusinessTypeEnum.USER_TRANSFER_IN),
    USER_TRANSFER_OUT(BusinessTypeEnum.USER_TRANSFER_OUT),

    USER_CRYPTO_DEPOSIT(BusinessTypeEnum.USER_CRYPTO_DEPOSIT),
    USER_FIAT_DEPOSIT(BusinessTypeEnum.USER_FIAT_DEPOSIT),
    USER_CRYPTO_WITHDRAW(BusinessTypeEnum.USER_CRYPTO_WITHDRAW),
    USER_FIAT_WITHDRAW(BusinessTypeEnum.USER_FIAT_WITHDRAW),

    USER_DAILY_BET_REBATE(BusinessTypeEnum.USER_DAILY_BET_REBATE),
    USER_DAILY_BET_COMMISSION(BusinessTypeEnum.USER_DAILY_BET_COMMISSION),

    USER_FUND_INCREASE(BusinessTypeEnum.USER_FUND_INCREASE),
    USER_FUND_DECREASE(BusinessTypeEnum.USER_FUND_DECREASE),

    USER_FUND_FREEZE(BusinessTypeEnum.USER_FUND_FREEZE),
    USER_FUND_UNFREEZE(BusinessTypeEnum.USER_FUND_UNFREEZE),

    USER_BONUS(BusinessTypeEnum.USER_BONUS),

    USER_PLAY_GAME_REWARDS(BusinessTypeEnum.USER_PLAY_GAME_REWARDS),
    USER_ITEM_ORDER(BusinessTypeEnum.USER_ITEM_ORDER),
    USER_TASK_REWARDS(BusinessTypeEnum.USER_TASK_REWARDS),

    ;

    private final BusinessTypeEnum businessTypeEnum;

    TransactionTypeEnum(BusinessTypeEnum businessTypeEnum) {
        this.businessTypeEnum = businessTypeEnum;
    }

    public static void main(String[] args) {
        for (TransactionTypeEnum e : TransactionTypeEnum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.businessTypeEnum.getType());
        }
        System.out.println();
        for (TransactionTypeEnum e : TransactionTypeEnum.values()) { //前端用 国际化
            System.out.printf("[TransactionTypeEnum.%s]: '%s.%s',\n", e.name(), "transaction_type_enum", e.name().toLowerCase());
        }
        System.out.println();
        for (TransactionTypeEnum e : TransactionTypeEnum.values()) { //前端用 国际化
            System.out.printf("\"%s.%s\": \"\",\n", "transaction_type_enum", e.name().toLowerCase());
        }

    }

}