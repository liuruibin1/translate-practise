package com.xxx.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.system.constants.SysRoleConstant;
import com.xxx.system.entity.SysRole;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.mapper.SysRoleMapper;
import com.xxx.system.vo.SysRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysRoleService extends BaseServiceImpl<SysRole, SysRoleVO, SysRoleMapper> {

    private final SysRoleMapper mapper;
    private final SysRolePermissionService sysRolePermissionService;
    private final SysUserRoleService sysUserRoleService;
    private final SysOperationLogService sysOperationLogService;

    public SysRoleService(
            SysRoleMapper mapper,
            SysRolePermissionService sysRolePermissionService,
            SysUserRoleService sysUserRoleService,
            SysOperationLogService sysOperationLogService) {
        this.mapper = mapper;
        this.sysRolePermissionService = sysRolePermissionService;
        this.sysUserRoleService = sysUserRoleService;
        this.sysOperationLogService = sysOperationLogService;
    }

    @Override
    public IPage<SysRoleVO> queryPage(Page<SysRoleVO> pageParam, SysRoleVO voParam) {
        IPage<SysRoleVO> page = mapper.queryPage(pageParam, voParam);
        buildReturn(page.getRecords(), voParam);
        return page;
    }

    @Override
    public List<SysRoleVO> queryList(SysRoleVO voParam) {
        List<SysRoleVO> voList = mapper.queryList(voParam);
        buildReturn(voList, voParam);
        return voList;
    }

    protected void buildReturn(List<SysRoleVO> voList, SysRoleVO voParam) {
        if (voParam != null) {
            if (voParam.getJoinPermissionIdList() != null && voParam.getJoinPermissionIdList()) {
                for (SysRoleVO sysRoleVO : voList) {
                    sysRoleVO.setPermissionIdList(sysRolePermissionService.queryDistinctPermissionIdByRoleId(sysRoleVO.getId()));
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response create(
            String name,
            Integer sort,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        //!!!静态验证
        if (StringUtils.isEmpty(name)) {
            return Response.error("角色名称必填");
        }
        if (ObjectUtils.isNull(sort)) {
            return Response.error("角色排序必填");
        }
        if (this.countByName(name) > 0) {  //名称不能存在
            return Response.error("角色名称已存在");
        }
        SysRole newEntity = new SysRole();
        newEntity.setId(getNewIdAsInteger());//id
        newEntity.setName(name);//name
        newEntity.setSort(sort);//sort
        newEntity.setIsEnabled(true);//is_enabled
        newEntity.setUpdateTs(new Date());//update_ts
        Response response = this.createImpl(newEntity);
        if (response.isSuccess()) {
            sysOperationLogService.recordInsert(getEntityNameEnum(), operatorType, operatorId, newEntity);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modify(
            Integer id,
            String name,
            Integer sort,
            Boolean isEnabled,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        //!!!数据库验证
        if (this.countByName(name) > 1) {  //名称不能改为相同的
            return Response.error("角色名称已存在");
        }
        SysRole dbEntity = queryById(id);
        if (StringUtils.isNotEmpty(name)) {//name
            dbEntity.setName(name);
        }
        if (ObjectUtils.isNotNull(sort)) {//sort
            dbEntity.setSort(sort);
        }
        if (ObjectUtils.isNotNull(isEnabled)) {//isEnabled
            dbEntity.setIsEnabled(isEnabled);
        }
        Response response = this.modifyByIdImpl(dbEntity);
        if (response.isSuccess()) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, dbEntity);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteById(
            Integer id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        if (id.equals(SysRoleConstant.SUPER_ADMINISTRATOR_ROLE_ID)) {
            return Response.error("超级管理员角色，无法删除");
        }
        if (id.equals(SysRoleConstant.MERCHANT_ROLE_ID)) {
            return Response.error("商户角色，无法删除");
        }
        if (sysRolePermissionService.countByRoleId(id) > 0) {
            return Response.error("角色权限正在引用，无法删除");
        }
        if (sysUserRoleService.countByRoleId(id) > 0) {
            return Response.error("存在用户正在引用，无法删除");
        }
        Response response = this.deleteByIdImpl(id);
        if (response.isSuccess()) {
            //删除中间表-角色权限
            //sysRolePermissionService.deleteByRoleId(id, operatorType, operatorId);
            //删除中间表-用户角色
            //sysUserRoleService.deleteByRoleId(id, operatorType, operatorId);
            SysRole deleteEntity = new SysRole();
            deleteEntity.setId(id);
            sysOperationLogService.recordDelete(getEntityNameEnum(), operatorType, operatorId, deleteEntity);
        }
        return response;
    }

    public List<Integer> queryDistinctIdBySysUserIdIsEnabledTrue(Long sysUserId) {
        return this.mapper.queryDistinctIdBySysUserIdIsEnabledTrue(sysUserId);
    }

    private Long countByName(String name) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getName, name);
        return this.count(queryWrapper);
    }

    @Override
    protected Integer getNewIdAsInteger() {
        Integer dbMaxId = mapper.getMaxId();
        return dbMaxId != null ? dbMaxId + 1 : 1;
    }

    protected SysOperationLogEntityEnum getEntityNameEnum() {
        return SysOperationLogEntityEnum.SYS_ROLE;
    }

}