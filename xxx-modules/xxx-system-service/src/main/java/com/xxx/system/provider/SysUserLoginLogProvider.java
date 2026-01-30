package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysUserLoginLog;
import com.xxx.system.service.SysUserLoginLogService;
import com.xxx.system.vo.SysUserLoginLogVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysUserLoginLogProvider implements ISysUserLoginLogProvider {

    final SysUserLoginLogService service;

    public SysUserLoginLogProvider(SysUserLoginLogService service) {
        this.service = service;
    }

    @Override
    public IPage<SysUserLoginLogVO> queryPage(Page<SysUserLoginLogVO> pageParam, SysUserLoginLogVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysUserLoginLogVO> queryList(SysUserLoginLogVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysUserLoginLogVO queryOne(SysUserLoginLogVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysUserLoginLogVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysUserLoginLog> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysUserLoginLog queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysUserLoginLog entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysUserLoginLog entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response create(String username, Boolean isSuccessful, String remark) {
        return service.create(username, isSuccessful, remark);
    }

}