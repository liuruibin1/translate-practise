package com.xxx.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.controller.BaseController;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.request.PageQuery;
import com.xxx.base.vo.Response;
import com.xxx.system.provider.ISysRoleProvider;
import com.xxx.system.vo.SysRoleVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/sys-role")
public class SysRoleController extends BaseController {

    @DubboReference
    ISysRoleProvider sysRoleProvider;

    @PreAuthorize("hasAuthority('sys_role:view')")
    @PostMapping("/page")
    @ResponseBody
    public IPage<SysRoleVO> page(@RequestBody PageQuery<SysRoleVO> pageQuery) {
        Page<SysRoleVO> pageParam = getPage(pageQuery);
        SysRoleVO voParam = dtoParamToVOParam(pageQuery.getDtoParam(), SysRoleVO.class);
        return this.sysRoleProvider.queryPage(pageParam, voParam);
    }

    @PreAuthorize("hasAuthority('sys_role:view')")
    @PostMapping("/list")
    @ResponseBody
    public List<SysRoleVO> list(@RequestBody SysRoleVO dtoParam) {
        SysRoleVO voParam = dtoParamToVOParam(dtoParam, SysRoleVO.class);
        return this.sysRoleProvider.queryList(voParam);
    }

    @PreAuthorize("hasAuthority('sys_role:save')")
    @GetMapping("/create")
    @ResponseBody
    public Response create(HttpServletRequest request,
                           @RequestParam String name,
                           @RequestParam Integer sort) {
        return sysRoleProvider.create(
                name,
                sort,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    @PreAuthorize("hasAuthority('sys_role:save')")
    @GetMapping("/update")
    @ResponseBody
    public Response update(HttpServletRequest request,
                           @RequestParam Integer id,
                           @RequestParam String name,
                           @RequestParam Integer sort,
                           @RequestParam Boolean isEnabled) {
        return sysRoleProvider.modify(
                id,
                name,
                sort,
                isEnabled,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    @PreAuthorize("hasAuthority('sys_role:delete')")
    @GetMapping("/delete")
    @ResponseBody
    public Response delete(HttpServletRequest request, @RequestParam Integer id) {
        return sysRoleProvider.deleteById(
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

}