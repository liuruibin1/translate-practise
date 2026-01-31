package com.xxx.enumerate;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

    MERCHANT(1, "商户"),
    USER(2, "用户"),
    //AFFILIATE(11, "商户分身"),
    ;

    private final Integer type;
    private final String description;

    UserTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static UserTypeEnum getByType(Integer value) {
        for (UserTypeEnum enumerate : UserTypeEnum.values()) {
            if (enumerate.getType().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}