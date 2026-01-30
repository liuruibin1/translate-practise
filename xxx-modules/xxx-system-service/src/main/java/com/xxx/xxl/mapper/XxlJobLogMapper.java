package com.xxx.xxl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.xxl.entity.XxlJobLog;
import org.apache.ibatis.annotations.Delete;

public interface XxlJobLogMapper extends BaseMapper<XxlJobLog> {

    @Delete("TRUNCATE TABLE xxl_job_log")
    int truncateTable();

}