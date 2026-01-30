package com.xxx.system.controller;

import com.xxx.base.controller.BaseController;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.provider.ISysRolePermissionProvider;
import com.xxx.system.request.SysRolePermissionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys/sys-role-permission")
public class SysRolePermissionController extends BaseController {

    @DubboReference
    ISysRolePermissionProvider sysRolePermissionProvider;

    @PreAuthorize("hasAuthority('sys_role_permission:save')")
    @PostMapping("/save")
    @ResponseBody
    public Response save(HttpServletRequest request, @RequestBody SysRolePermissionDTO dtoParam) {
        return sysRolePermissionProvider.saveByRoleIdPermissionIdList(
                dtoParam.getRoleId(),
                dtoParam.getPermissionIdList(),
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

}