package com.xxx.system.request;

import lombok.Data;

import java.util.List;

@Data
public class SysUserRoleDTO {
    private Long sysUserId;
    private List<Integer> roleIdList;
}