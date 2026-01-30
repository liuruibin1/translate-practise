package com.xxx.open.enumerate;

import com.xxx.common.core.enumerate.BizErrorEnum;
import lombok.Getter;

@Getter
public enum OpenApiBizErrorEnum {

    _1001(1001, "缺少必填参数"),
    _1002(1002, "签名必填"),
    //_1003(1003, "时间戳无效"),
    _1005(1005, "无效签名"),

    //_1011(1011, "商户无效"),
    //_1012(1012, "用户无效"),
    //_1013(1013, "用户密码无效"),
    _1015(1015, "用户转账金额不能小于0"),
    _1016(1016, "转账方式不存在"),
    _1017(1017, "货币不存在"),
    _1018(1018, "用户转账记录已存在"),
    _1019(1019, "用户资金不足"),
    _1020(1020, "商户资金不足"),
    _1021(1021, "商户不存在"),
    _1022(1022, "用户已经存在"),
    _1023(1023, "用户不存在"),
    _1024(1024, "用户被禁止"),
    _1025(1025, "日期格式不正确"),
    _1026(1026, "非用户账户无法登录"),

    _1027(1027, "时间已超过60天"),


    _9998(9998, "内部异常"),
    _9999(9999, "操作异常"),

    ;
    
    private final Integer code;
    private final String description;
    private final BizErrorEnum bizErrorEnum;

    OpenApiBizErrorEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
        this.bizErrorEnum = null;
    }

    public static OpenApiBizErrorEnum getByCode(Integer code) {
        for (OpenApiBizErrorEnum enumerate : OpenApiBizErrorEnum.values()) {
            if (enumerate.getCode().equals(code)) {
                return enumerate;
            }
        }
        return null;
    }

}