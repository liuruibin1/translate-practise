package com.xxx.system.provider;

import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysUser;
import com.xxx.system.vo.SysUserVO;

import java.util.List;

public interface ISysUserProvider extends IProvider<SysUser, SysUserVO> {

    Response create(SysUserVO vo, OperatorTypeEnum operatorType, Long operatorId);

    Response modifyById(SysUserVO vo, OperatorTypeEnum operatorType, Long operatorId);

    Response deleteById(Long id, OperatorTypeEnum operatorType, Long operatorId);

    Response createForMerchant(String username, String password, Long merchantUserId, OperatorTypeEnum operatorType, Long operatorId);

    Response updatePasswordById(String password, Long id, OperatorTypeEnum operatorType, Long operatorId);

    Response updateIsEnabledById(Boolean isEnabled, Long id, OperatorTypeEnum operatorType, Long operatorId);

    Response updateIsDeletedById(Boolean isDeleted, Long id, OperatorTypeEnum operatorType, Long operatorId);

    Response updatePasswordByMerchantUserId(String password, Long merchantUserId, OperatorTypeEnum operatorType, Long operatorId);

    Response batchUpdateIsEnabledByMerchantUserIdList(Boolean isEnabled, List<Long> merchantUserIdList, OperatorTypeEnum operatorType, Long operatorId);

    SysUser queryByUsernameEmail(String username, String email);

    SysUser queryOneByUsername(String username);

}
