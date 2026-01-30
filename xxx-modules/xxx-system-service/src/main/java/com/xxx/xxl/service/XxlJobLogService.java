package com.xxx.xxl.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.xxl.entity.XxlJobLog;
import com.xxx.xxl.mapper.XxlJobLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class XxlJobLogService extends ServiceImpl<XxlJobLogMapper, XxlJobLog> {

    private final XxlJobLogMapper mapper;

    public XxlJobLogService(XxlJobLogMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void truncateTable() {
        this.mapper.truncateTable();
    }

}
