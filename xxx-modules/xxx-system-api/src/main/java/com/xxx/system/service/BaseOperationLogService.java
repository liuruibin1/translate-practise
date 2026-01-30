package com.xxx.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.provider.ISysOperationLogProvider;
import org.apache.dubbo.config.annotation.DubboReference;

public abstract class BaseOperationLogService<Entity, VO extends Entity, Mapper extends BaseMapper<Entity>> extends BaseServiceImpl<Entity, VO, Mapper> {

    @DubboReference
    protected ISysOperationLogProvider sysOperationLogProvider;

    protected abstract SysOperationLogEntityEnum getEntityNameEnum();

    protected void recordInsert(
            ISysOperationLogProvider sysOperationLogProvider,
            OperatorTypeEnum operatorType,
            Long operatorId,
            Object data) {
        sysOperationLogProvider.recordInsert(getEntityNameEnum(), operatorType, operatorId, data);
    }

    protected void recordUpdate(
            ISysOperationLogProvider sysOperationLogProvider,
            OperatorTypeEnum operatorType,
            Long operatorId,
            Object data) {
        sysOperationLogProvider.recordUpdate(getEntityNameEnum(), operatorType, operatorId, data);
    }

    protected void recordDelete(
            ISysOperationLogProvider sysOperationLogProvider,
            OperatorTypeEnum operatorType,
            Long operatorId,
            Object data) {
        sysOperationLogProvider.recordDelete(getEntityNameEnum(), operatorType, operatorId, data);
    }

}
