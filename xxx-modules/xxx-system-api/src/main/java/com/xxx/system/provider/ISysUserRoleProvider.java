package com.xxx.system.provider;

import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysUserRole;
import com.xxx.system.vo.SysUserRoleVO;

import java.util.List;

public interface ISysUserRoleProvider extends IProvider<SysUserRole, SysUserRoleVO> {

    Response saveBySysUserIdRoleIdList(Long sysUserId, List<Integer> roleIdList);

    Response saveByRoleIdSysUserIdList(Integer roleId, List<Long> sysUserIdList);

    List<SysUserRole> queryListBySysUserIdList(List<Long> sysUserIdList);

}