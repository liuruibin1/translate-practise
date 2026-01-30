package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysUser;
import com.xxx.system.service.SysUserService;
import com.xxx.system.vo.SysUserVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysUserProvider implements ISysUserProvider {

    final SysUserService service;

    public SysUserProvider(SysUserService service) {
        this.service = service;
    }

    @Override
    public IPage<SysUserVO> queryPage(Page<SysUserVO> pageParam, SysUserVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysUserVO> queryList(SysUserVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysUserVO queryOne(SysUserVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysUserVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysUser> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysUser queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysUser entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysUser entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response create(SysUserVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        return service.create(vo, operatorType, operatorId);
    }

    @Override
    public Response modifyById(SysUserVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        return service.modifyById(vo, operatorType, operatorId);
    }

    @Override
    public Response deleteById(Long id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.deleteById(id, operatorType, operatorId);
    }

    @Override
    public Response createForMerchant(String username, String password, Long merchantUserId, OperatorTypeEnum operatorType, Long operatorId) {
        return service.createForMerchant(username, password, merchantUserId, operatorType, operatorId);
    }

    @Override
    public Response updatePasswordById(String password, Long id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updatePasswordById(password, id, operatorType, operatorId);
    }

    @Override
    public Response updateIsEnabledById(Boolean isEnabled, Long id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updateIsEnabledById(isEnabled, id, operatorType, operatorId);
    }

    @Override
    public Response updateIsDeletedById(Boolean isDeleted, Long id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updateIsDeletedById(isDeleted, id, operatorType, operatorId);
    }

    @Override
    public Response updatePasswordByMerchantUserId(String password, Long merchantUserId, OperatorTypeEnum operatorType, Long operatorId) {
        return service.updatePasswordByMerchantUserId(password, merchantUserId, operatorType, operatorId);
    }

    @Override
    public Response batchUpdateIsEnabledByMerchantUserIdList(Boolean isEnabled, List<Long> merchantUserIdList, OperatorTypeEnum operatorType, Long operatorId) {
        return service.batchUpdateIsEnabledByMerchantUserIdList(isEnabled, merchantUserIdList, operatorType, operatorId);
    }

    @Override
    public SysUser queryOneByUsername(String username) {
        return service.queryOneByUsername(username);
    }

    @Override
    public SysUser queryByUsernameEmail(String username, String email) {
        return service.queryOneByUsernameEmail(username, email);
    }

}