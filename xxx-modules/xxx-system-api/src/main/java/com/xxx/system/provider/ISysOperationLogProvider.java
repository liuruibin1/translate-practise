package com.xxx.system.provider;

import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.system.entity.SysOperationLog;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.vo.SysOperationLogVO;

public interface ISysOperationLogProvider extends IProvider<SysOperationLog, SysOperationLogVO> {

    void recordInsert(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data);

    void recordUpdate(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data);

    void recordDelete(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data);

}
