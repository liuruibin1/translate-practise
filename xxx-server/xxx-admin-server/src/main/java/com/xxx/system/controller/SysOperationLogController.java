package com.xxx.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.controller.BaseController;
import com.xxx.base.request.PageQuery;
import com.xxx.system.provider.ISysOperationLogProvider;
import com.xxx.system.vo.SysOperationLogVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys/sys-operation-log")
public class SysOperationLogController extends BaseController {

    @DubboReference
    ISysOperationLogProvider sysOperationLogProvider;

    @PreAuthorize("hasAuthority('sys_operation_log:view')")
    @PostMapping("/page")
    @ResponseBody
    public IPage<SysOperationLogVO> page(@RequestBody PageQuery<SysOperationLogVO> pageQuery) {
        Page<SysOperationLogVO> pageParam = getPage(pageQuery);
        return this.sysOperationLogProvider.queryPage(pageParam, pageQuery.getDtoParam());
    }

}