package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysUserRole;
import com.xxx.system.service.SysUserRoleService;
import com.xxx.system.vo.SysUserRoleVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysUserRoleProvider implements ISysUserRoleProvider {

    final SysUserRoleService service;

    public SysUserRoleProvider(SysUserRoleService service) {
        this.service = service;
    }

    @Override
    public IPage<SysUserRoleVO> queryPage(Page<SysUserRoleVO> pageParam, SysUserRoleVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysUserRoleVO> queryList(SysUserRoleVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysUserRoleVO queryOne(SysUserRoleVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysUserRoleVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysUserRole> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysUserRole queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysUserRole entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysUserRole entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response saveBySysUserIdRoleIdList(Long sysUserId, List<Integer> roleIdList) {
        return service.saveBySysUserIdRoleIdList(sysUserId, roleIdList);
    }

    @Override
    public Response saveByRoleIdSysUserIdList(Integer roleId, List<Long> sysUserIdList) {
        return service.saveByRoleIdSysUserIdList(roleId, sysUserIdList);
    }

    @Override
    public List<SysUserRole> queryListBySysUserIdList(List<Long> sysUserIdList) {
        return service.queryListBySysUserIdList(sysUserIdList);
    }

}