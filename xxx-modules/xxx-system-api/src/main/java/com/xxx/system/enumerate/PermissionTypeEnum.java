package com.xxx.system.enumerate;

import lombok.Getter;

@Getter
public enum PermissionTypeEnum {

    FOLDER(1, "folder", "目录"),
    PAGE(3, "page", "页面"),
    PRIVILEGE(5, "privilege", "权限");

    private final Integer value;

    private final String labelEn;

    private final String labelCn;

    PermissionTypeEnum(Integer value, String labelEn, String labelCn) {
        this.value = value;
        this.labelEn = labelEn;
        this.labelCn = labelCn;
    }

}