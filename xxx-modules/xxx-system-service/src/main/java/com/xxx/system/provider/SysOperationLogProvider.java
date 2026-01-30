package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysOperationLog;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.service.SysOperationLogService;
import com.xxx.system.vo.SysOperationLogVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysOperationLogProvider implements ISysOperationLogProvider {

    final SysOperationLogService service;

    public SysOperationLogProvider(SysOperationLogService service) {
        this.service = service;
    }

    @Override
    public IPage<SysOperationLogVO> queryPage(Page<SysOperationLogVO> pageParam, SysOperationLogVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysOperationLogVO> queryList(SysOperationLogVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysOperationLogVO queryOne(SysOperationLogVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysOperationLogVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysOperationLog> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysOperationLog queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysOperationLog entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysOperationLog entity) {
        return service.modifyById(entity);
    }

    @Override
    public void recordInsert(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data) {
        service.recordInsert(entityName, operatorType, operatorId, data);
    }

    @Override
    public void recordUpdate(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data) {
        service.recordUpdate(entityName, operatorType, operatorId, data);
    }

    @Override
    public void recordDelete(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data) {
        service.recordDelete(entityName, operatorType, operatorId, data);
    }

}