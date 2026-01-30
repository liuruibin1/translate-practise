package com.xxx.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.controller.BaseController;
import com.xxx.base.request.PageQuery;
import com.xxx.system.provider.ISysUserLoginLogProvider;
import com.xxx.system.vo.SysUserLoginLogVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys/sys-user-login-log")
public class SysUserLoginLogController extends BaseController {

    @DubboReference
    ISysUserLoginLogProvider sysUserLoginLogProvider;

    @PreAuthorize("hasAuthority('sys_user_login_log:view')")
    @PostMapping("/page")
    @ResponseBody
    public IPage<SysUserLoginLogVO> page(@RequestBody PageQuery<SysUserLoginLogVO> pageQuery) {
        Page<SysUserLoginLogVO> pageParam = getPage(pageQuery);
        return this.sysUserLoginLogProvider.queryPage(pageParam, pageQuery.getDtoParam());
    }

}