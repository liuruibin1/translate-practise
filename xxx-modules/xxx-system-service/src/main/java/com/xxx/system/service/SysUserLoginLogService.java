package com.xxx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.base.vo.Response;
import com.xxx.framework.id.utils.SnowflakeIdUtil;
import com.xxx.system.entity.SysUserLoginLog;
import com.xxx.system.mapper.SysUserLoginLogMapper;
import com.xxx.system.vo.SysUserLoginLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SysUserLoginLogService extends BaseServiceImpl<SysUserLoginLog, SysUserLoginLogVO, SysUserLoginLogMapper> {

    private final SysUserLoginLogMapper mapper;
    private final SnowflakeIdUtil snowflakeIdUtil;

    public SysUserLoginLogService(SysUserLoginLogMapper mapper, SnowflakeIdUtil snowflakeIdUtil) {
        this.mapper = mapper;
        this.snowflakeIdUtil = snowflakeIdUtil;
    }

    @Override
    public IPage<SysUserLoginLogVO> queryPage(Page<SysUserLoginLogVO> pageParam, SysUserLoginLogVO voParam) {
        return mapper.queryPage(pageParam, voParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response create(String username, Boolean isSuccessful, String remark) {
        SysUserLoginLog newEntity = new SysUserLoginLog();
        newEntity.setId(snowflakeIdUtil.generate());
        newEntity.setUsername(username);
        newEntity.setIsSuccessful(isSuccessful);
        newEntity.setRemark(remark);
        newEntity.setCreateTs(new Date());
        return this.createImpl(newEntity);
    }

}