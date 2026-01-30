package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.system.entity.SysUserLoginLog;
import com.xxx.system.vo.SysUserLoginLogVO;
import org.apache.ibatis.annotations.Param;

public interface SysUserLoginLogMapper extends BaseMapper<SysUserLoginLog> {

    IPage<SysUserLoginLogVO> queryPage(@Param("page") Page<SysUserLoginLogVO> page, @Param("paramObj") SysUserLoginLogVO paramObj);

}