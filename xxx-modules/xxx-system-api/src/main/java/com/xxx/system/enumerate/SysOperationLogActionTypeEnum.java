package com.xxx.system.enumerate;

import lombok.Getter;

@Getter
public enum SysOperationLogActionTypeEnum {

    INSERT(1, "新增"),
    UPDATE(3, "修改"),
    DELETE(5, "删除");

    private final Integer value;

    private final String description;

    SysOperationLogActionTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}