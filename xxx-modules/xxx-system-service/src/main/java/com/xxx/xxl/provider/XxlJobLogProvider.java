package com.xxx.xxl.provider;

import com.xxx.xxl.service.XxlJobLogService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class XxlJobLogProvider implements IXxlJobLogProvider {

    final XxlJobLogService xxlJobLogService;

    public XxlJobLogProvider(XxlJobLogService xxlJobLogService) {
        this.xxlJobLogService = xxlJobLogService;
    }

    @Override
    public void truncateTable() {
        xxlJobLogService.truncateTable();
    }
}
