package com.xxx.system.provider;

import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysPermission;
import com.xxx.system.vo.SysPermissionVO;

import java.util.List;

public interface ISysPermissionProvider extends IProvider<SysPermission, SysPermissionVO> {

    Response create(SysPermissionVO vo, OperatorTypeEnum operatorType, Long operatorId);

    Response modifyById(SysPermissionVO vo, OperatorTypeEnum operatorType, Long operatorId);

    Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId);

    List<String> queryDistinctCodeBySysUserIdIsEnabledTrue(Long sysUserId);

    List<SysPermissionVO> queryBySysUserIdIsEnabledTrue(Long sysUserId);

    Response updateIsVisibleById(Boolean isVisible, Integer id, OperatorTypeEnum operatorType, Long operatorId);

    Response updateIsCacheById(Boolean isCache, Integer id, OperatorTypeEnum operatorType, Long operatorId);

    Response updateIsEnabledById(Boolean isEnabled, Integer id, OperatorTypeEnum operatorType, Long operatorId);

}