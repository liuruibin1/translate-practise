package com.xxx.system.request;

import lombok.Data;

import java.util.List;

@Data
public class SysUserRoleDTO2 {
    private Integer roleId;
    private List<Long> sysUserIdList;
}