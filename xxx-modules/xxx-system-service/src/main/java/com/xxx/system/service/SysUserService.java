package com.xxx.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.common.core.exception.BusinessRuntimeException;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.system.constants.SysRoleConstant;
import com.xxx.system.entity.SysUser;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.mapper.SysUserMapper;
import com.xxx.system.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.xxx.common.core.enumerate.BizErrorEnum._10_001;
import static com.xxx.common.core.enumerate.BizErrorEnum._551_003;

@Service
public class SysUserService extends BaseServiceImpl<SysUser, SysUserVO, SysUserMapper> {

    private final SysUserMapper mapper;
    private final SysUserRoleService sysUserRoleService;
    private final SysOperationLogService sysOperationLogService;

    public SysUserService(
            SysUserMapper mapper,
            SysUserRoleService sysUserRoleService,
            SysOperationLogService sysOperationLogService) {
        this.mapper = mapper;
        this.sysUserRoleService = sysUserRoleService;
        this.sysOperationLogService = sysOperationLogService;
    }

    @Override
    public IPage<SysUserVO> queryPage(Page<SysUserVO> pageParam, SysUserVO voParam) {
        return mapper.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysUserVO> queryList(SysUserVO voParam) {
        return mapper.queryList(voParam);
    }

    @Override
    public SysUserVO queryOne(SysUserVO voParam) {
        return mapper.queryOne(voParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response create(SysUserVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        SysUser dbEntity = queryOneByUsername(vo.getUsername());
        if (dbEntity != null) {
            return Response.error("System user already exists");
        }
        Response response = validationField(vo, true);
        if (!response.isSuccess()) {
            return response;
        }
        vo.setId(getNewIdAsLong());
        vo.setUpdateTs(new Date());
        response = this.createImpl(vo);
        if (response.isSuccess()) {
            sysOperationLogService.recordInsert(getEntityNameEnum(), operatorType, operatorId, vo);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modifyById(SysUserVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        Response response = validationField(vo, false);
        if (!response.isSuccess()) {
            return response;
        }
        SysUser dbEntity = queryById(vo.getId());
        //dept_id
        if (ObjectUtils.isNotNull(vo.getDeptId())) {
            dbEntity.setDeptId(vo.getDeptId());
        }
        //username
        if (StringUtils.isNotEmpty(vo.getUsername())) {
            dbEntity.setUsername(vo.getUsername());
        }
        //password //不允许修改
        //nick_name
        if (StringUtils.isNotEmpty(vo.getNickName())) {
            dbEntity.setNickName(vo.getNickName());
        }
        //email - 允许为空
        dbEntity.setEmail(vo.getEmail());
        //phone_number - 允许为空
        dbEntity.setPhoneNumber(vo.getPhoneNumber());
        //avatar - 允许为空
        dbEntity.setAvatar(vo.getAvatar());
        //is_enabled
        if (ObjectUtils.isNotNull(vo.getIsEnabled())) {
            dbEntity.setIsEnabled(vo.getIsEnabled());
        }
        //is_deleted
        if (ObjectUtils.isNotNull(vo.getIsDeleted())) {
            dbEntity.setIsDeleted(vo.getIsDeleted());
        }
        //remark - 允许为空
        dbEntity.setRemark(vo.getRemark());
        //create_ts
        //update_ts
        dbEntity.setUpdateTs(new Date());
        //
        response = this.modifyByIdImpl(dbEntity);
        if (response.isSuccess()) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, vo);
        }
        return response;
    }

    @Override
    protected Response validationField(SysUser entity, boolean isCreate) {
        //是否创建
        if (isCreate) {
            //dept_id
            if (ObjectUtils.isNull(entity.getDeptId())) {
                return Response.error("dept id is required");
            }
            //username
            if (StringUtils.isEmpty(entity.getUsername())) {
                return Response.error("Username is required");
            }
            //password
            if (StringUtils.isEmpty(entity.getPassword())) {
                return Response.error("Password is required");
            }
            //nick_name
            if (StringUtils.isEmpty(entity.getNickName())) {
                return Response.error("Nick Name is required");
            }
            //is_enabled
            if (ObjectUtils.isNull(entity.getIsEnabled())) {
                return Response.error("Is enabled is required");
            }
            //is_deleted
            if (ObjectUtils.isNull(entity.getIsDeleted())) {
                return Response.error("Is deleted is required");
            }
        }
        //username
        if (StringUtils.isNotEmpty(entity.getUsername())) {
            SysUser dbEntity = this.queryOneByUsername(entity.getUsername());
            if (ObjectUtils.isNotNull(dbEntity) && !dbEntity.getId().equals(entity.getId())) {
                return Response.error("Username already exists");
            }
        }
        //nick_name
        if (StringUtils.isNotEmpty(entity.getNickName())) {
            SysUser dbEntity = this.queryOneByNickName(entity.getNickName());
            if (ObjectUtils.isNotNull(dbEntity) && !dbEntity.getId().equals(entity.getId())) {
                return Response.error("Nick name already exists");
            }
        }
        //email
        if (StringUtils.isNotEmpty(entity.getEmail())) {
            SysUser dbEntity = this.queryOneByEmail(entity.getEmail());
            if (ObjectUtils.isNotNull(dbEntity) && !dbEntity.getId().equals(entity.getId())) {
                return Response.error("Email already exists");
            }
        }
        return super.validationField(entity, isCreate);
    }

    public Response deleteById(Long id, OperatorTypeEnum operatorType, Long operatorId) {
        SysUser updateEntity = new SysUser();
        updateEntity.setId(id);
        updateEntity.setIsDeleted(true);
        updateEntity.setUpdateTs(new Date());
        if (mapper.updateIsDeletedById(updateEntity.getIsDeleted(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            //删除中间表-用户角色
            sysUserRoleService.deleteBySysUserId(id);
            return Response.success();
        } else {
            return Response.error(_551_003);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response createForMerchant(String username,
                                      String password,
                                      Long merchantUserId,
                                      OperatorTypeEnum operatorType,
                                      Long operatorId) {
        if (StringUtils.isEmpty(username)) {
            return Response.error("Username is required");
        }
        if (StringUtils.isEmpty(password)) {
            return Response.error("Password is required");
        }
        if (merchantUserId == null) {
            return Response.error("Password is required");
        }
        Date currentDate = new Date();
        SysUser newEntity = new SysUser();
        newEntity.setId(getNewIdAsLong());//id
        newEntity.setDeptId(1);//dept_id
        newEntity.setUsername(username);//username
        newEntity.setMerchantUserId(merchantUserId);//merchant_user_id
        newEntity.setPassword(password);//password
        newEntity.setNickName(username);//nick_name
        //email
        //phone_number
        //telegram_id
        //avatar
        newEntity.setIsEnabled(true);//is_enabled
        newEntity.setIsDeleted(false);//is_deleted
        //remark
        newEntity.setCreateTs(currentDate);//create_ts
        newEntity.setUpdateTs(currentDate);//update_ts
        //1
        Response response = this.createImpl(newEntity);
        if (response.isSuccess()) {
            sysOperationLogService.recordInsert(getEntityNameEnum(), operatorType, operatorId, newEntity);
        }
        //2 用户角色
        response = sysUserRoleService.saveBySysUserIdRoleIdList(newEntity.getId(), List.of(SysRoleConstant.MERCHANT_ROLE_ID));
        if (!response.isSuccess()) {
            throw new BusinessRuntimeException(response.getMessage());
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updatePasswordById(
            String password,
            Long id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysUserVO vo = new SysUserVO();
        vo.setPassword(password);
        Response response = validationField(vo, false);
        if (!response.isSuccess()) {
            return response;
        }
        SysUser updateEntity = new SysUser();
        updateEntity.setId(id);
        updateEntity.setPassword(password);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updatePasswordById(updateEntity.getPassword(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            //cache 不缓存字段
            return Response.success();
        } else {
            return Response.error("update password fail");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateIsEnabledById(
            Boolean isEnabled,
            Long id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysUser updateEntity = new SysUser();
        updateEntity.setId(id);
        updateEntity.setIsEnabled(isEnabled);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updateIsEnabledById(updateEntity.getIsEnabled(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            return Response.success();
        } else {
            return Response.error(_551_003);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateIsDeletedById(
            Boolean isDeleted,
            Long id,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysUser updateEntity = new SysUser();
        updateEntity.setId(id);
        updateEntity.setIsDeleted(isDeleted);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updateIsDeletedById(updateEntity.getIsDeleted(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            return Response.success();
        } else {
            return Response.error(_551_003);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updatePasswordByMerchantUserId(
            String password,
            Long merchantUserId,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        SysUser dbEntity = this.queryOneByMerchantUserId(merchantUserId);
        if (dbEntity == null) {
            return Response.error(_10_001);
        }
        SysUserVO vo = new SysUserVO();
        vo.setPassword(password);
        Response response = validationField(vo, false);
        if (!response.isSuccess()) {
            return response;
        }
        SysUser updateEntity = new SysUser();
        updateEntity.setId(dbEntity.getId());
        updateEntity.setPassword(password);
        updateEntity.setUpdateTs(new Date());
        if (this.mapper.updatePasswordById(updateEntity.getPassword(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
            sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
            //cache 不缓存字段
            return Response.success();
        } else {
            return Response.error("update password fail");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response batchUpdateIsEnabledByMerchantUserIdList(
            Boolean isEnabled,
            List<Long> merchantUserIdList,
            OperatorTypeEnum operatorType,
            Long operatorId) {
        StringBuilder stringBuilder = new StringBuilder();
        for (long merchantUserId : merchantUserIdList) {
            SysUser dbEntity = this.queryOneByMerchantUserId(merchantUserId);
            if (dbEntity != null) {
                SysUser updateEntity = new SysUser();
                updateEntity.setId(dbEntity.getId());
                updateEntity.setIsEnabled(isEnabled);
                updateEntity.setUpdateTs(new Date());
                if (this.mapper.updateIsEnabledById(updateEntity.getIsEnabled(), updateEntity.getUpdateTs(), updateEntity.getId()) > 0) {
                    sysOperationLogService.recordUpdate(getEntityNameEnum(), operatorType, operatorId, updateEntity);
                } else {
                    stringBuilder.append(dbEntity.getId()).append(", ");
                }
            }
        }
        if (!stringBuilder.isEmpty()) {
            return Response.error(_551_003 + ": " + stringBuilder.toString());
        } else {
            return Response.success();
        }
    }

    public SysUser queryOneByUsernameEmail(String username, String email) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username)
                .eq(SysUser::getEmail, email)
                .last(" LIMIT 1");
        return this.getOne(queryWrapper);
    }

    public SysUser queryOneByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username)
                .last(" LIMIT 1");
        return this.getOne(queryWrapper);
    }

    public Long countByDeptId(Serializable deptId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDeptId, deptId);
        return this.count(queryWrapper);
    }

    @Override
    protected Long getNewIdAsLong() {
        Long dbMaxId = mapper.getMaxId();
        return dbMaxId != null ? dbMaxId + 1 : 1;
    }

    protected SysOperationLogEntityEnum getEntityNameEnum() {
        return SysOperationLogEntityEnum.SYS_USER;
    }

    private SysUser queryOneByNickName(String nickName) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getNickName, nickName)
                .last(" LIMIT 1");
        return this.getOne(queryWrapper);
    }

    private SysUser queryOneByEmail(String email) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getEmail, email)
                .last(" LIMIT 1");
        return this.getOne(queryWrapper);
    }

    public SysUser queryOneByMerchantUserId(Long merchantUserId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getMerchantUserId, merchantUserId)
                .last(" LIMIT 1");
        return this.getOne(queryWrapper);
    }

}