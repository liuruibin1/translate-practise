package com.xxx.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.common.core.exception.BusinessRuntimeException;
import com.xxx.system.entity.SysRolePermission;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.mapper.SysRolePermissionMapper;
import com.xxx.system.vo.SysRolePermissionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRolePermissionService extends BaseServiceImpl<SysRolePermission, SysRolePermissionVO, SysRolePermissionMapper> {

    private final SysRolePermissionMapper mapper;
    private final SysOperationLogService sysOperationLogService;

    public SysRolePermissionService(SysRolePermissionMapper mapper, SysOperationLogService sysOperationLogService) {
        this.mapper = mapper;
        this.sysOperationLogService = sysOperationLogService;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response saveByRoleIdPermissionIdList(
            Integer roleId,
            List<Integer> permissionIdList,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        //1 删除
        deleteByRoleId(roleId, operatorType, operatorId);
        Integer entityId = getNewIdAsInteger();
        Response response = Response.success();
        for (Integer permissionId : permissionIdList) {
            if (countByRoleIdPermissionId(roleId, permissionId) < 1) {
                SysRolePermission newEntity = new SysRolePermission();
                newEntity.setId(entityId);
                newEntity.setRoleId(roleId);
                newEntity.setPermissionId(permissionId);
                response = this.createImpl(newEntity); //2 创建
                if (!response.isSuccess()) {
                    throw new BusinessRuntimeException("Save role permission exception");
                } else {
                    sysOperationLogService.recordInsert(getEntityNameEnum(), operatorType, operatorId, newEntity);
                }
            }
            entityId++;
        }
        return response;
    }

    protected List<Integer> queryDistinctPermissionIdByRoleId(Integer roleId) {
        return this.mapper.queryDistinctPermissionIdByRoleId(roleId);
    }

    protected long countByRoleId(Integer roleId) {
        LambdaQueryWrapper<SysRolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRolePermission::getRoleId, roleId);
        return this.count(queryWrapper);
    }

    protected long countByPermissionId(Integer permissionId) {
        LambdaQueryWrapper<SysRolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRolePermission::getPermissionId, permissionId);
        return this.count(queryWrapper);
    }

    @Override
    protected Integer getNewIdAsInteger() {
        Integer dbMaxId = mapper.getMaxId();
        return dbMaxId != null ? dbMaxId + 1 : 1;
    }

    protected SysOperationLogEntityEnum getEntityNameEnum() {
        return SysOperationLogEntityEnum.SYS_ROLE_PERMISSION;
    }

    private void deleteByRoleId(Integer roleId, OperatorTypeEnum operatorType, Long operatorId) {
        int effectRow = this.mapper.deleteByRoleId(roleId);
        if (effectRow > 0) {
            SysRolePermission deleteEntity = new SysRolePermission();
            deleteEntity.setRoleId(roleId);
            sysOperationLogService.recordDelete(getEntityNameEnum(), operatorType, operatorId, deleteEntity);
        }
    }

    private long countByRoleIdPermissionId(Integer roleId, Integer permissionId) {
        LambdaQueryWrapper<SysRolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRolePermission::getRoleId, roleId)
                .eq(SysRolePermission::getPermissionId, permissionId);
        return this.count(queryWrapper);
    }

}