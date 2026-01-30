package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysRole;
import com.xxx.system.service.SysRoleService;
import com.xxx.system.vo.SysRoleVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysRoleProvider implements ISysRoleProvider {

    final SysRoleService service;

    public SysRoleProvider(SysRoleService service) {
        this.service = service;
    }

    @Override
    public IPage<SysRoleVO> queryPage(Page<SysRoleVO> pageParam, SysRoleVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysRoleVO> queryList(SysRoleVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysRoleVO queryOne(SysRoleVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysRoleVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysRole> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysRole queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysRole entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysRole entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response create(
            String name,
            Integer sort,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        return service.create(name, sort, operatorType, operatorId);
    }

    @Override
    public Response modify(
            Integer id,
            String name,
            Integer sort,
            Boolean isEnabled,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        return service.modify(id, name, sort, isEnabled, operatorType, operatorId);
    }

    @Override
    public Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.deleteById(id, operatorType, operatorId);
    }

    @Override
    public List<Integer> queryDistinctIdBySysUserIdIsEnabledTrue(Long sysUserId) {
        return service.queryDistinctIdBySysUserIdIsEnabledTrue(sysUserId);
    }

}
