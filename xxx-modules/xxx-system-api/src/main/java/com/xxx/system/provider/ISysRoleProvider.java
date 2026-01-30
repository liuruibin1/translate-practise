package com.xxx.system.provider;

import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysRole;
import com.xxx.system.vo.SysRoleVO;

import java.util.List;

public interface ISysRoleProvider extends IProvider<SysRole, SysRoleVO> {

    Response create(
            String name,
            Integer sort,
            OperatorTypeEnum operatorType,
            Long operatorId);

    Response modify(
            Integer id,
            String name,
            Integer sort,
            Boolean isEnabled,
            OperatorTypeEnum operatorType,
            Long operatorId);

    Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId);

    List<Integer> queryDistinctIdBySysUserIdIsEnabledTrue(Long sysUserId);

}
