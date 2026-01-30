package com.xxx.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.system.constants.SysDeptConstant;
import com.xxx.system.entity.SysDept;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.mapper.SysDeptMapper;
import com.xxx.system.vo.SysDeptVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysDeptService extends BaseServiceImpl<SysDept, SysDeptVO, SysDeptMapper> {

    private final SysDeptMapper mapper;
    private final SysUserService sysUserService;
    private final SysOperationLogService sysOperationLogService;

    public SysDeptService(
            SysDeptMapper mapper,
            SysUserService sysUserService,
            SysOperationLogService sysOperationLogService) {
        this.mapper = mapper;
        this.sysUserService = sysUserService;
        this.sysOperationLogService = sysOperationLogService;
    }

    @Override
    public IPage<SysDeptVO> queryPage(Page<SysDeptVO> pageParam, SysDeptVO voParam) {
        return mapper.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysDeptVO> queryList(SysDeptVO voParam) {
        return mapper.queryList(voParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response create(SysDeptVO vo, OperatorTypeEnum operatorType, Long operatorId) {
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
        Date currentDate = new Date();
        vo.setUpdateTs(currentDate);
        response = this.createImpl(vo);
        if (response.isSuccess()) {
            sysOperationLogService.recordInsert(getEntityNameEnum(), operatorType, operatorId, vo);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modifyById(SysDeptVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        Response response = validationField(vo, false);
        if (!response.isSuccess()) {
            return response;
        }
        SysDept dbEntity = queryById(vo.getId());
        //parent_id
        if (ObjectUtils.isNotNull(vo.getParentId())) {
            dbEntity.setParentId(vo.getParentId());
        }
        //name
        if (StringUtils.isNotEmpty(vo.getName())) {
            dbEntity.setName(vo.getName());
        }
        //sort
        if (ObjectUtils.isNotNull(vo.getSort())) {
            dbEntity.setSort(vo.getSort());
        }
        response = this.modifyByIdImpl(dbEntity);
        if (response.isSuccess()) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, vo);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        if (id.equals(SysDeptConstant.HIGHEST_DEPT_ID)) {
            return Response.error("the highest department cannot be deleted");
        }
        if (countByParentId(id) > 0) {
            return Response.error("child referencing");
        }
        if (sysUserService.countByDeptId(id) > 0) {
            return Response.error("system user referencing");
        }
        Response response = this.deleteByIdImpl(id);
        if (response.isSuccess()) {
            SysDept deleteEntity = new SysDept();
            deleteEntity.setId(id);
            sysOperationLogService.recordDelete(getEntityNameEnum(), operatorType, operatorId, deleteEntity);
        }
        return response;
    }

    @Override
    protected Response validationField(SysDept entity, boolean isCreate) {
        //是否创建
        if (isCreate) {
            // name
            if (StringUtils.isEmpty(entity.getName())) {
                return Response.error("name is required");
            }
        }
        // parent_id
        SysDept parentEntity = queryById(entity.getParentId());
        if (ObjectUtils.isNull(parentEntity)) {
            return Response.error("superior dept non-existent");
        }
        if (ObjectUtils.isNotNull(entity.getParentId())) {
            if (entity.getParentId().equals(entity.getId())) {
                return Response.error("the superior dept can't be oneself");
            }
        }
        //name
        if (StringUtils.isNotEmpty(entity.getName())) {
            if (countByName(entity.getName()) > 0) {
                return Response.error("name already exists");
            }
        }
        return super.validationField(entity, isCreate);
    }

    private long countByParentId(Integer parentId) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getParentId, parentId);
        return this.count(queryWrapper);
    }

    private long countByName(String name) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDept::getName, name);
        return this.count(queryWrapper);
    }

    @Override
    protected Integer getNewIdAsInteger() {
        Integer dbMaxId = mapper.getMaxId();
        return dbMaxId != null ? dbMaxId + 1 : 1;
    }

    protected SysOperationLogEntityEnum getEntityNameEnum() {
        return SysOperationLogEntityEnum.SYS_DEPT;
    }

}