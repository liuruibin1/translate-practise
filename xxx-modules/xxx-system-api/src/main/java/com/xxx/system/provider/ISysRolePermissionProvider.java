package com.xxx.system.provider;

import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysRolePermission;
import com.xxx.system.vo.SysRolePermissionVO;

import java.util.List;

public interface ISysRolePermissionProvider extends IProvider<SysRolePermission, SysRolePermissionVO> {

    Response saveByRoleIdPermissionIdList(
            Integer roleId,
            List<Integer> permissionIdList,
            OperatorTypeEnum operatorType,
            Long operatorId);

}