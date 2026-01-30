package com.xxx.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.system.entity.SysPermission;
import com.xxx.system.entity.SysRole;
import com.xxx.system.enumerate.PermissionTypeEnum;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.mapper.SysPermissionMapper;
import com.xxx.system.vo.SysPermissionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.xxx.common.core.enumerate.BizErrorEnum._551_001;

@Service
public class SysPermissionService extends BaseServiceImpl<SysPermission, SysPermissionVO, SysPermissionMapper> {

    private final SysPermissionMapper mapper;
    private final SysRolePermissionService sysRolePermissionService;
    private final SysOperationLogService sysOperationLogService;

    public SysPermissionService(
            SysPermissionMapper mapper,
            SysRolePermissionService sysRolePermissionService,
            SysOperationLogService sysOperationLogService) {
        this.mapper = mapper;
        this.sysRolePermissionService = sysRolePermissionService;
        this.sysOperationLogService = sysOperationLogService;
    }

    @Override
    public List<SysPermissionVO> queryList(SysPermissionVO voParam) {
        return mapper.queryList(voParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response create(SysPermissionVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        Response response = validationField(vo, true);
        if (!response.isSuccess()) {
            return response;
        }
        //id
        vo.setId(getNewIdAsInteger());
        //sort
        if (ObjectUtils.isNull(vo.getSort())) {
            vo.setSort(0);
        }
        //is_visible
        if (ObjectUtils.isNull(vo.getIsVisible())) {
            vo.setIsVisible(true);
        }
        vo.setUpdateTs(new Date());
        response = this.createImpl(vo);
        if (response.isSuccess()) {
            sysOperationLogService.recordInsert(getEntityNameEnum(), operatorType, operatorId, vo);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modifyById(SysPermissionVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        Response response = validationField(vo, false);
        if (!response.isSuccess()) {
            return response;
        }
        SysPermission dbEntity = queryById(vo.getId());
        //parent_id
        if (ObjectUtils.isNotNull(vo.getParentId())) {
            dbEntity.setParentId(vo.getParentId());
        }
        //type
        if (ObjectUtils.isNotNull(vo.getType())) {
            dbEntity.setType(vo.getType());
        }
        //name
        if (StringUtils.isNotEmpty(vo.getName())) {
            dbEntity.setName(vo.getName());
        }
        //sort
        if (ObjectUtils.isNotNull(vo.getSort())) {
            dbEntity.setSort(vo.getSort());
        }
        //is_visible
        if (ObjectUtils.isNotNull(vo.getIsVisible())) {
            dbEntity.setIsVisible(vo.getIsVisible());
        }
        //url_path - 允许为空
        dbEntity.setUrlPath(vo.getUrlPath());
        //page_component - 允许为空
        dbEntity.setPageComponent(vo.getPageComponent());
        //icon - 允许为空
        dbEntity.setIcon(vo.getIcon());
        //code - 允许为空
        dbEntity.setCode(vo.getCode());
        //is_page_frame
        if (ObjectUtils.isNotNull(vo.getIsPageFrame())) {
            dbEntity.setIsPageFrame(vo.getIsPageFrame());
        }
        //url_query - 允许为空
        dbEntity.setUrlQuery(vo.getUrlQuery());
        //is_cache
        if (ObjectUtils.isNotNull(vo.getIsCache())) {
            dbEntity.setIsCache(vo.getIsCache());
        }
        //is_enabled
        if (ObjectUtils.isNotNull(vo.getIsEnabled())) {
            dbEntity.setIsEnabled(vo.getIsEnabled());
        }
        dbEntity.setUpdateTs(new Date());
        //根据 type 清空指定字段
        if (PermissionTypeEnum.FOLDER.getValue().equals(dbEntity.getType())) {
            dbEntity.setPageComponent(null);
            dbEntity.setCode(null);
        }
        if (PermissionTypeEnum.PAGE.getValue().equals(dbEntity.getType())) {
            dbEntity.setIcon(null);
            dbEntity.setCode(null);
        }
        if (PermissionTypeEnum.PRIVILEGE.getValue().equals(dbEntity.getType())) {
            dbEntity.setUrlPath(null);
            dbEntity.setPageComponent(null);
            dbEntity.setIcon(null);
            dbEntity.setUrlQuery(null);
        }
        response = this.modifyByIdImpl(dbEntity);
        if (response.isSuccess()) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, vo);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        if (countByParentId(id) > 0) {
            return Response.error("child referencing");
        }
        if (sysRolePermissionService.countByPermissionId(id) > 0) {
            return Response.error("role permission referencing");
        }
        Response response = this.deleteByIdImpl(id);
        if (response.isSuccess()) {
            //删除中间表-角色权限
            //sysRolePermissionService.deleteByPermissionId(id, operatorType, operatorId);
            //
            SysRole deleteEntity = new SysRole();
            deleteEntity.setId(id);
            sysOperationLogService.recordDelete(getEntityNameEnum(), operatorType, operatorId, deleteEntity);
        }
        return response;
    }

    @Override
    protected Response validationField(SysPermission entity, boolean isCreate) {
        //是否创建
        if (isCreate) {
            // type
            if (ObjectUtils.isNull(entity.getType())) {
                return Response.error("type is required");
            }
            // name
            if (StringUtils.isEmpty(entity.getName())) {
                return Response.error("name is required");
            }
        }
        // parent_id
        SysPermission parentEntity = queryById(entity.getParentId());
        if (ObjectUtils.isNull(parentEntity)) {
            return Response.error("superior non-existent");
        }
        if (ObjectUtils.isNotNull(entity.getParentId())) {
            if (entity.getParentId().equals(entity.getId())) {
                return Response.error("the superior can't be oneself");
            }
        }
        //name
        /*if (StringUtils.isNotEmpty(vo.getName())) {
            SysPermission dbEntity = this.queryByName(vo.getName());
            if (ObjectUtils.isNotNull(dbEntity) && !dbEntity.getId().equals(vo.getId())) {
                return Response.error("name already exists");
            }
        }*/
        //url_path
        if (StringUtils.isEmpty(entity.getUrlPath())
                && (entity.getType().equals(PermissionTypeEnum.FOLDER.getValue())
                || entity.getType().equals(PermissionTypeEnum.PAGE.getValue()))) {
            return Response.error("type is "
                    + PermissionTypeEnum.FOLDER.getValue()
                    + " or " + PermissionTypeEnum.PAGE.getValue()
                    + ", url path is required");
        }
        //page_component
        if (StringUtils.isEmpty(entity.getPageComponent()) && entity.getType().equals(PermissionTypeEnum.PAGE.getValue())) {
            return Response.error("type is "
                    + PermissionTypeEnum.PAGE.getValue()
                    + ", page component is required");
        }
        //code
        if (StringUtils.isEmpty(entity.getCode()) && entity.getType().equals(PermissionTypeEnum.PRIVILEGE.getValue())) {
            return Response.error("type is "
                    + PermissionTypeEnum.PRIVILEGE.getValue()
                    + ", privilege code is required");
        }
        return super.validationField(entity, isCreate);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateIsVisibleById(
            Boolean isVisible,
            Integer id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysPermission updateEntity = new SysPermission();
        updateEntity.setId(id);
        updateEntity.setIsVisible(isVisible);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updateIsVisibleById(updateEntity.getIsVisible(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            return Response.success();
        } else {
            return Response.error(_551_001);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateIsCacheById(
            Boolean isCache,
            Integer id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysPermission updateEntity = new SysPermission();
        updateEntity.setId(id);
        updateEntity.setIsCache(isCache);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updateIsCacheById(updateEntity.getIsVisible(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            return Response.success();
        } else {
            return Response.error(_551_001);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateIsEnabledById(
            Boolean isEnabled,
            Integer id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysPermission updateEntity = new SysPermission();
        updateEntity.setId(id);
        updateEntity.setIsEnabled(isEnabled);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updateIsEnabledById(updateEntity.getIsEnabled(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            return Response.success();
        } else {
            return Response.error(_551_001);
        }
    }

    public List<String> queryDistinctCodeBySysUserIdIsEnabledTrue(Long sysUserId) {
        return this.mapper.queryDistinctCodeBySysUserIdIsEnabledTrue(sysUserId);
    }

    public List<SysPermissionVO> queryBySysUserIdIsEnabledTrue(Long sysUserId) {
        return this.mapper.queryBySysUserIdIsEnabledTrue(sysUserId);
    }

    private long countByParentId(Integer parentId) {
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPermission::getParentId, parentId);
        return this.count(queryWrapper);
    }

    @Override
    protected Integer getNewIdAsInteger() {
        Integer dbMaxId = mapper.getMaxId();
        return dbMaxId != null ? dbMaxId + 1 : 1;
    }

    protected SysOperationLogEntityEnum getEntityNameEnum() {
        return SysOperationLogEntityEnum.SYS_PERMISSION;
    }

}