package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysPermission;
import com.xxx.system.service.SysPermissionService;
import com.xxx.system.vo.SysPermissionVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysPermissionProvider implements ISysPermissionProvider {

    final SysPermissionService service;

    public SysPermissionProvider(SysPermissionService service) {
        this.service = service;
    }

    @Override
    public IPage<SysPermissionVO> queryPage(Page<SysPermissionVO> pageParam, SysPermissionVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysPermissionVO> queryList(SysPermissionVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysPermissionVO queryOne(SysPermissionVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysPermissionVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysPermission> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysPermission queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysPermission entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysPermission entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response create(SysPermissionVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        return service.create(vo, operatorType, operatorId);
    }

    @Override
    public Response modifyById(SysPermissionVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        return service.modifyById(vo, operatorType, operatorId);
    }

    @Override
    public Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.deleteById(id, operatorType, operatorId);
    }

    @Override
    public List<String> queryDistinctCodeBySysUserIdIsEnabledTrue(Long sysUserId) {
        return service.queryDistinctCodeBySysUserIdIsEnabledTrue(sysUserId);
    }

    @Override
    public List<SysPermissionVO> queryBySysUserIdIsEnabledTrue(Long sysUserId) {
        return service.queryBySysUserIdIsEnabledTrue(sysUserId);
    }

    @Override
    public Response updateIsVisibleById(Boolean isVisible, Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updateIsVisibleById(isVisible, id, operatorType, operatorId);
    }

    @Override
    public Response updateIsCacheById(Boolean isCache, Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updateIsCacheById(isCache, id, operatorType, operatorId);
    }

    @Override
    public Response updateIsEnabledById(Boolean isEnabled, Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updateIsEnabledById(isEnabled, id, operatorType, operatorId);
    }
}
