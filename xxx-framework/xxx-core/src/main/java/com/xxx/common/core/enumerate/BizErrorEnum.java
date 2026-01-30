package com.xxx.common.core.enumerate;

import com.xxx.open.enumerate.OpenApiBizErrorEnum;
import lombok.Getter;

import static com.xxx.open.enumerate.OpenApiBizErrorEnum.*;

@Getter
public enum BizErrorEnum {

    //user
    _10_001(10_001, "User non-existent", "用户不存在"),
    _10_002(10_002, "User already exists", "用户已经存在", _1022),
    _10_003(10_003, "Not the superior of the user", "不是用户的上级"),
    _10_004(10_004, "The username format is incorrect", "用户名格式错误"),

    //user_fund
    _11_001(11_001, "User fund creation exception", "用户资金创建异常"),
    _11_002(11_002, "User fund update exception", "用户资金更新异常"),
    _11_003(11_003, "User has insufficient funds", "用户资金不足", _1019),
    _11_004(11_004, "User fund non-existent", "用户资金不存在"),
    _11_005(11_005, "User fund update operation is invalid", "用户资金更新操作无效"),
    _11_006(11_006, "USER fund amount must be greater than 0", "用户资金数额必须大于0"),
    _11_007(11_007, "User fund update or user fund flow creation exception", "用户资金更新或用户资金流创建异常"),

    //user_fund_flow
    _12_001(12_001, "User fund flow remark length cannot exceed 256 characters", "用户资金流备注长度不能超过256"),
    _12_002(12_002, "User fund flow creation exception", "用户资金流创建异常"),

    //user_game_order
    _13_001(13_001, "User game order update draw status, settlement status exception", "注单更新开奖状态结算状态异常"),
    _13_002(13_002, "User game order bet counts error", "注单注数错误"),
    _13_003(13_003, "User game order cancel exception", "注单取消异常"),
    _13_005(13_005, "User game order already settled", "注单已经结算"),


    //bet_type
    _31_001(31_001, "Bet type does not match", "玩法不匹配"),
    _31_002(31_002, "Bet type non-existent", "玩法不存在"),
    _31_003(31_003, "Bet Type does not match the game", "玩法与游戏不匹配"),

    //bet_option_group
    _32_001(32_001, "Bet option group is required", "选项组是必需的"),
    _32_002(32_002, "Bet option group non-existent", "选项组不存在"),
    _32_003(32_003, "Bet option group must not be duplicated", "投注选项组不能重复"),
    _32_005(32_005, "Bet option group does not match this bet type", "投注选项组不匹配该玩法"),

    //bet_option
    _33_001(33_001, "Bet option is required", "选项是必需的"),
    _33_002(33_002, "Bet option non-existent", "选项不存在"),
    _33_003(33_003, "Bet option must not be duplicated", "投注选项不能重复"),
    _33_004(33_004, "Bet option does not match this bet type", "投注选项不匹配该玩法"),
    _33_005(33_005, "Bet options are mutually exclusive", "投注选项是相互排斥的"),

    //odds
    _34_001(34_001, "Odds must ≥1 and ≤9999.9999", "赔率必须 ≥1 且 ≤99999.9999"),
    _34_003(34_003, "Odds should be greater than the previous odds", "赔率要大于之前的赔率"),
    _34_004(34_004, "Odds should be less than the previous odds", "赔率要小于之前的赔率"),

    //game
    _60_001(60_001, "Game non-existent", "游戏不存在"),
    _60_002(60_002, "Game already crashed", "游戏已经崩溃"),

    //game_round
    _61_001(61_001, "Game round drawn", "游戏回合已开奖"),
    _61_002(61_002, "Game round non-existent", "游戏回合不存在"),
    _61_003(61_003, "Game round revoke draw failed", "游戏回合撤销开奖失败"),
    _61_005(61_005, "Game round is not closed", "游戏回合不是封盘"),
    _61_006(61_006, "Game round been over six hours", "游戏回合已超过 %s 小时"),
    _61_007(61_007, "Game round has not started yet", "游戏回合未开始"),

    //game_service_vendor
    _62_001(62_001, "Game service vendor exception", "游戏服务商异常"),
    _62_002(62_002, "Game service vendor non-existent", "游戏服务商不存在"),

    //game_provider
    _63_001(63_001, "Game provider currency non-existent", "游戏供应商货币不存在"),

    //room
    _69_001(69_001, "User has not entered the game room", "用户未进入游戏房间"),

    //currency
    _71_001(71_005, "Currency non-existent", "货币不存在"),

    //fiat_currency
    //_72_001 法定货币

    //crypto_currency
    //_73_001 加密货币

    //fiat_channel_service_vendor
    _75_001(75_001, "Fiat channel service vendor non-existent", "法币渠道服务商不存在"),

    _500_001(500_001, "Login token is invalid", "登录令牌无效"),
    _500_002(500_002, "Login token is invalid or has expired", "登录令牌无效或已过期"),
    _500_500(500_500, "System error, please try again later", "系统服务异常，请稍后再试"),

    _550_001(550_001, "Creation not allowed", "不允许创建", _9999),
    _550_002(550_002, "Deletion not allowed", "不允许删除", _9999),
    _550_003(550_003, "Modification not allowed", "不允许修改", _9999),

    _551_001(551_001, "Data was not created", "数据未创建", _9999),
    _551_002(551_002, "Data was not deleted", "数据未删除", _9999),
    _551_003(551_003, "Data was not modified", "数据未修改", _9999),
    ;

    private final Integer code;
    private final String descriptionEn;
    private final String descriptionCn;
    private final OpenApiBizErrorEnum openApiBizErrorEnum;

    BizErrorEnum(Integer code, String descriptionEn, String descriptionCn) {
        this.code = code;
        this.descriptionEn = descriptionEn;
        this.descriptionCn = descriptionCn;
        this.openApiBizErrorEnum = null;
    }

    BizErrorEnum(Integer code, String descriptionEn, String descriptionCn, OpenApiBizErrorEnum openApiBizErrorEnum) {
        this.code = code;
        this.descriptionEn = descriptionEn;
        this.descriptionCn = descriptionCn;
        this.openApiBizErrorEnum = openApiBizErrorEnum;
    }

    public static BizErrorEnum getByCode(Integer code) {
        for (BizErrorEnum enumerate : BizErrorEnum.values()) {
            if (enumerate.getCode().equals(code)) {
                return enumerate;
            }
        }
        return null;
    }

}