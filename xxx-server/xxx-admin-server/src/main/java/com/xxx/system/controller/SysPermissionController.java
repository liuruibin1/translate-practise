package com.xxx.system.controller;

import com.xxx.base.controller.BaseController;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.system.bo.ReactRouterBO;
import com.xxx.system.constants.SysPermissionConstant;
import com.xxx.system.entity.SysPermission;
import com.xxx.system.provider.ISysPermissionProvider;
import com.xxx.system.utils.ReactRouterUtil;
import com.xxx.system.utils.SysPermissionUtil;
import com.xxx.system.vo.SysPermissionVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys/sys-permission")
public class SysPermissionController extends BaseController {

    @DubboReference
    ISysPermissionProvider sysPermissionProvider;

    @PreAuthorize("hasAuthority('sys_permission:view')")
    @PostMapping("/tree")
    @ResponseBody
    public List<SysPermissionVO> queryTree(@RequestBody SysPermissionVO dtoParam) {
        SysPermissionVO voParam = dtoParamToVOParam(dtoParam, SysPermissionVO.class);
        List<SysPermissionVO> voList = sysPermissionProvider.queryList(voParam);
        boolean isIncludeRoot = voParam.getIsIncludeRoot();
        List<SysPermissionVO> newVOList = build(voList, isIncludeRoot);
        List<SysPermissionVO> newVOListSort = newVOList.stream().sorted(Comparator.comparing(SysPermission::getSort)).collect(Collectors.toList());
        return SysPermissionUtil.buildTree(newVOListSort);
    }

    @PostMapping("/router-tree")
    @ResponseBody
    public List<ReactRouterBO> routerTree(HttpServletRequest request) {
        Long currentSysUserId = getSysUserId(request);
        if (ObjectUtils.isNotNull(currentSysUserId)) {
            List<SysPermissionVO> voList = sysPermissionProvider.queryBySysUserIdIsEnabledTrue(currentSysUserId);
            List<SysPermissionVO> newVOList = build(voList, false);
            List<SysPermissionVO> newVOListSort = newVOList.stream()
                    .sorted(Comparator.comparing(SysPermission::getSort))
                    .collect(Collectors.toList());
            List<SysPermissionVO> treeVOList = SysPermissionUtil.buildTree(newVOListSort);
            return ReactRouterUtil.buildTree(treeVOList);
        } else {
            return null;
        }
    }

    @PreAuthorize("hasAuthority('sys_permission:save')")
    @PostMapping("/save")
    @ResponseBody
    public Response save(HttpServletRequest request, @RequestBody SysPermissionVO dtoParam) {
        SysPermissionVO voParam = dtoParamToVOParam(dtoParam, SysPermissionVO.class);
        if (ObjectUtils.isNotNull(voParam.getId())) {
            return sysPermissionProvider.modifyById(
                    voParam,
                    OperatorTypeEnum.SYSTEM,
                    getSysUserId(request));
        } else {
            return sysPermissionProvider.create(
                    voParam,
                    OperatorTypeEnum.SYSTEM,
                    getSysUserId(request));
        }
    }

    @PreAuthorize("hasAuthority('sys_permission:delete')")
    @GetMapping("/delete")
    @ResponseBody
    public Response delete(HttpServletRequest request, @RequestParam Integer id) {
        return sysPermissionProvider.deleteById(
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    @PreAuthorize("hasAuthority('sys_permission:updateIsVisible')")
    @GetMapping("/update-is-visible")
    @ResponseBody
    public Response updateIsVisible(HttpServletRequest request, @RequestParam Boolean isVisible, @RequestParam Integer id) {
        return sysPermissionProvider.updateIsVisibleById(
                isVisible,
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    @PreAuthorize("hasAuthority('sys_permission:updateIsCache')")
    @GetMapping("/update-is-cache")
    @ResponseBody
    public Response updateIsCache(HttpServletRequest request, @RequestParam Boolean isCache, @RequestParam Integer id) {
        return sysPermissionProvider.updateIsCacheById(
                isCache,
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    @PreAuthorize("hasAuthority('sys_permission:updateIsEnabled')")
    @GetMapping("/update-is-enabled")
    @ResponseBody
    public Response updateIsEnabled(HttpServletRequest request, @RequestParam Boolean isEnabled, @RequestParam Integer id) {
        return sysPermissionProvider.updateIsEnabledById(
                isEnabled,
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    private List<SysPermissionVO> build(List<SysPermissionVO> voList, boolean isIncludeRoot) {
        if (!isIncludeRoot) {
            return voList.stream()
                    .filter(i -> !SysPermissionConstant.PERMISSION_ROOT_ID.equals(i.getId()))
                    .collect(Collectors.toList());
        } else {
            return voList;
        }
    }

}