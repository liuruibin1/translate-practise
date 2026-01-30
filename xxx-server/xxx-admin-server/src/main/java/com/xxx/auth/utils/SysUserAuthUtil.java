package com.xxx.auth.utils;

import com.xxx.auth.constants.SysPermissionConstant;
import com.xxx.common.core.utils.StringUtils;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;

public class SysUserAuthUtil {

    /**
     * 判断是否包含权限
     *
     * @param permissionList 权限列表
     * @param permission     权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermission(Collection<String> permissionList, String permission) {
        return permissionList
                .stream()
                .filter(StringUtils::hasText)
                .anyMatch(i -> SysPermissionConstant.ALL_PERMISSION.contains(i) || PatternMatchUtils.simpleMatch(i, permission));
    }

    //    /**
    //     * 判断是否包含角色
    //     *
    //     * @param roleList 角色列表
    //     * @param role     角色
    //     * @return 用户是否具备某角色权限
    //     */
    //    public static boolean hasRole(Collection<String> roleList, String role) {
    //        return roleList
    //                .stream()
    //                .filter(StringUtils::hasText)
    //                .anyMatch(i -> SysUserAuthConstant.SUPER_ADMIN.contains(i) || PatternMatchUtils.simpleMatch(i, role));
    //    }

}
