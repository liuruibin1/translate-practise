package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysRolePermission;
import com.xxx.system.service.SysRolePermissionService;
import com.xxx.system.vo.SysRolePermissionVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysRolePermissionProvider implements ISysRolePermissionProvider {

    final SysRolePermissionService service;

    public SysRolePermissionProvider(SysRolePermissionService service) {
        this.service = service;
    }

    @Override
    public IPage<SysRolePermissionVO> queryPage(Page<SysRolePermissionVO> pageParam, SysRolePermissionVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysRolePermissionVO> queryList(SysRolePermissionVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysRolePermissionVO queryOne(SysRolePermissionVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysRolePermissionVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysRolePermission> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysRolePermission queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysRolePermission entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysRolePermission entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response saveByRoleIdPermissionIdList(
            Integer roleId,
            List<Integer> permissionIdList,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        return service.saveByRoleIdPermissionIdList(roleId, permissionIdList, operatorType, operatorId);
    }

}