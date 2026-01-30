package com.xxx.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.system.entity.SysUserRole;
import com.xxx.system.mapper.SysUserRoleMapper;
import com.xxx.system.vo.SysUserRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserRoleService extends BaseServiceImpl<SysUserRole, SysUserRoleVO, SysUserRoleMapper> {

    private final SysUserRoleMapper mapper;

    public SysUserRoleService(SysUserRoleMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response saveBySysUserIdRoleIdList(Long sysUserId, List<Integer> roleIdList) {
        if (ObjectUtils.isNotNull(sysUserId)) {
            this.deleteBySysUserId(sysUserId);
            //this.mapper.deleteBySysUserId(sysUserId);
        }
        List<SysUserRole> newEntities = new ArrayList<>();
        if (roleIdList != null && !roleIdList.isEmpty()) {
            Integer entityId = getNewIdAsInteger();
            for (Integer roleId : roleIdList) {
                SysUserRole newEntity = new SysUserRole();
                newEntity.setId(entityId);
                newEntity.setRoleId(roleId);
                if (ObjectUtils.isNotNull(sysUserId)) {
                    newEntity.setSysUserId(sysUserId);
                    newEntity.setRoleId(roleId);
                    newEntities.add(newEntity);
                    entityId++;
                }
            }
            return newEntities.isEmpty() ? Response.success() : (this.saveBatch(newEntities) ? Response.success() : Response.error());
        } else {
            deleteBySysUserId(sysUserId);
            return Response.success();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response saveByRoleIdSysUserIdList(Integer roleId, List<Long> sysUserIdList) {
        List<SysUserRole> newEntities = new ArrayList<>();
        Integer entityId = getNewIdAsInteger();
        if (ObjectUtils.isNotNull(sysUserIdList) && !sysUserIdList.isEmpty()) {
            for (Long sysUserId : sysUserIdList) {
                SysUserRole newEntity = new SysUserRole();
                newEntity.setId(entityId);
                newEntity.setRoleId(roleId);
                newEntity.setSysUserId(sysUserId);
                newEntities.add(newEntity);
                entityId++;
            }
        }
        return this.saveBatch(newEntities) ? Response.success() : Response.error();
    }

    public int deleteBySysUserId(Long sysUserId) {
        if (ObjectUtils.isNotNull(sysUserId)) {
            return this.mapper.deleteBySysUserId(sysUserId);
        }
        return 0;
    }

    public int deleteByRoleId(Integer roleId) {
        return this.mapper.deleteByRoleId(roleId);
    }

    public List<SysUserRole> queryListBySysUserIdList(List<Long> userIdList) {
        return this.mapper.queryListBySysUserIdList(userIdList);
    }

    protected long countByRoleId(Integer roleId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getRoleId, roleId);
        return this.count(queryWrapper);
    }

    //    private long countBySysUserIdRoleId(Long sysUserId, Integer roleId) {
    //        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
    //        queryWrapper.eq(SysUserRole::getSysUserId, sysUserId)
    //                .eq(SysUserRole::getRoleId, roleId);
    //        return this.count(queryWrapper);
    //    }

    //    private long countByUserIdRoleId(Long userId, Integer roleId) {
    //        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
    //        queryWrapper.eq(SysUserRole::getUserId, userId)
    //                .eq(SysUserRole::getRoleId, roleId);
    //        return this.count(queryWrapper);
    //    }

    @Override
    protected Integer getNewIdAsInteger() {
        Integer dbMaxId = mapper.getMaxId();
        return dbMaxId != null ? dbMaxId + 1 : 1;
    }

}