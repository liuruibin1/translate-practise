package com.xxx.system.provider;

import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysDept;
import com.xxx.system.vo.SysDeptVO;

public interface ISysDeptProvider extends IProvider<SysDept, SysDeptVO> {

    Response create(SysDeptVO vo, OperatorTypeEnum operatorType, Long operatorId);

    Response modifyById(SysDeptVO vo, OperatorTypeEnum operatorType, Long operatorId);

    Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId);

}
