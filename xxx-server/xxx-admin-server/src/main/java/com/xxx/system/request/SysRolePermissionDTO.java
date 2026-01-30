package com.xxx.system.request;

import lombok.Data;

import java.util.List;

@Data
public class SysRolePermissionDTO {

    private Integer roleId;
    private List<Integer> permissionIdList;

}