package com.xxx.auth.controller;

import com.xxx.auth.bo.UserLoginBO;
import com.xxx.auth.response.LoginResponse;
import com.xxx.auth.service.UserLoginService;
import com.xxx.base.controller.BaseController;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.user.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.xxx.common.core.enumerate.BizErrorEnum._500_500;

@Component
public class BaseAuthController extends BaseController {

    @Resource
    protected UserLoginService userLoginDataService;

    protected LoginResponse afterLogin(User dbUser) {
        UserLoginBO userLoginBO = new UserLoginBO();
        dbUser.setPassword("*"); //TODO 敏感数据
        userLoginBO.setUser(dbUser);
        //
        //2 登录
        String jwtToken = userLoginDataService.getTokenAndRefreshCache(userLoginBO);
        if (StringUtils.isEmpty(jwtToken)) {
            return LoginResponse.error(_500_500);
        }
        //记录
//        userLoginLogProvider.create(dbUser.getId(), true, "");//记录登录
        return LoginResponse.success(jwtToken);
    }

}