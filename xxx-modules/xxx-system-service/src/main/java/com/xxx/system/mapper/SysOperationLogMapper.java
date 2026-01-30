package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.system.entity.SysOperationLog;
import com.xxx.system.vo.SysOperationLogVO;
import org.apache.ibatis.annotations.Param;

public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    IPage<SysOperationLogVO> queryPage(@Param("page") Page<SysOperationLogVO> page, @Param("paramObj") SysOperationLogVO paramObj);

}