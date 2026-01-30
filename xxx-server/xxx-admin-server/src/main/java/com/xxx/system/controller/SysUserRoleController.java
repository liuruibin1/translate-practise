package com.xxx.system.controller;

import com.xxx.base.controller.BaseController;
import com.xxx.base.vo.Response;
import com.xxx.system.provider.ISysUserRoleProvider;
import com.xxx.system.request.SysUserRoleDTO;
import com.xxx.system.request.SysUserRoleDTO2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys/sys-user-role")
public class SysUserRoleController extends BaseController {

    @DubboReference
    ISysUserRoleProvider sysUserRoleProvider;

    @PreAuthorize("hasAuthority('sys_user_role:save')")
    @PostMapping("/save-by-sys-user-id-role-id-list")
    @ResponseBody
    public Response saveBySysUserIdRoleIdList(@RequestBody SysUserRoleDTO dtoParam) {
        return sysUserRoleProvider.saveBySysUserIdRoleIdList(dtoParam.getSysUserId(), dtoParam.getRoleIdList());
    }

    @PreAuthorize("hasAuthority('sys_user_role:save')")
    @PostMapping("/save-by-role-id-sys-user-id-list")
    @ResponseBody
    public Response saveByRoleIdSysUserIdList(@RequestBody SysUserRoleDTO2 dtoParam) {
        return sysUserRoleProvider.saveByRoleIdSysUserIdList(dtoParam.getRoleId(), dtoParam.getSysUserIdList());
    }

}