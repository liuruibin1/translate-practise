package com.xxx.system.enumerate;

import lombok.Getter;

@Getter
public enum SysOperationLogEntityEnum {

    USER(1, "用户"),
    USER_FUND(2, "用户资金"),
    USER_FUND_FREEZE(3, "用户资金冻结"),
    //USER_LOTTERY_ORDER(3, "用户彩票订单"),
    USER_SETTINGS(6, "用户设置"),
    //USER_GROUP_SETTINGS(7, "用户组设置"),
    USER_CRYPTO_DEPOSIT(8, "用户加密存款"),
    USER_CRYPTO_WITHDRAW(9, "用户加密提现"),
    USER_FIAT_DEPOSIT(10, "用户法币存款"),
    USER_FIAT_WITHDRAW(11, "用户法币提现"),

    //ADMIN_ALLOCATE_FEE(20, "管理员分发手续费"),
    //ADMIN_CRYPTO_ACCOUNT_FUND(21, "管理员加密账户资金"),
    //ADMIN_COLLECTION_ACCOUNT(22, "管理员归集加密账户"),

    //LOTTERY_TYPE(51, "彩票分类"),
    //LOTTERY_TYPE_ODDS(52, "彩票分类赔率"),
    //LOTTERY(53, "彩票"),
    //LOTTERY_ODDS(54, "彩票赔率"),

    GAME(61, "游戏"),
    GAME_ODDS(62, "游戏赔率"),
    GAME_REBATE_COMMISSION_CONFIG(63, "游戏返水返佣配置"),

    ROLLOVER_CONFIG(71, "流水要求配置"),
    ROLLOVER_BONUS_CONFIG(72, "流水要求奖励配置"),
    WEEK_RAFFLE_CONFIG(73, "每周抽奖配置"),
    //COMMISSION_CONFIG(74, "返佣配置"),
    //VIP_LEVEL_CONFIG(75, "VIP等级配置"),
    //USER_DEPOSIT_CONFIG(76, "用户充值配置"),

    CURRENCY(81, "货币"),
    CRYPTO_CURRENCY(82, "加密货币"),
    FIAT_CURRENCY(83, "法定货币"),

    SYS_DEPT(91, "系统部门"),
    SYS_PERMISSION(92, "系统权限"),
    SYS_ROLE_PERMISSION(93, "系统角色权限"),
    SYS_ROLE(95, "系统角色"),
    //SYS_USER_ROLE(96, "系统用户角色"),
    SYS_USER(97, "系统用户");

    private final Integer id;
    private final String description;

    SysOperationLogEntityEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static void main(String[] args) {
        for (SysOperationLogEntityEnum e : SysOperationLogEntityEnum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.getId());
        }
        System.out.println();
        for (SysOperationLogEntityEnum e : SysOperationLogEntityEnum.values()) { //后端用
            System.out.printf("[SysOperationLogEntityEnum.%s]: '%s',\n", e.name(), e.getDescription());
        }
    }

}
