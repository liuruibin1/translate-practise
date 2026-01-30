package com.xxx.system.provider;

import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysUserLoginLog;
import com.xxx.system.vo.SysUserLoginLogVO;

public interface ISysUserLoginLogProvider extends IProvider<SysUserLoginLog, SysUserLoginLogVO> {

    Response create(String username, Boolean isSuccessful, String remark);
}
