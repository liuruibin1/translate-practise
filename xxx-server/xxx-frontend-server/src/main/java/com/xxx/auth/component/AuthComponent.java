//package com.xxx.auth.component;
//
//import com.xxx.base.controller.BaseController;
//import com.xxx.framework.jwt.configuration.properties.JWTProperties;
//import com.xxx.task.enumerate.TaskEnum;
//import com.xxx.user.provider.IUserLoginLogProvider;
//import com.xxx.user.provider.IUserTaskRecordProvider;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AuthComponent extends BaseController {
//
//    //@DubboReference
//    //IUserPreferenceProvider userPreferenceProvider;
//
//    @DubboReference
//    IUserLoginLogProvider userLoginLogProvider;
//
//    @DubboReference
//    IUserTaskRecordProvider userTaskRecordProvider;
//
//    //@DubboReference
//    //IUserMetricsProvider userMetricsProvider;
//
//    //private final UserLoginDataService userLoginDataService;
//
//    public AuthComponent(JWTProperties jwtProperties) {
//        this.jwtProperties = jwtProperties;
//        //this.userLoginDataService = userLoginDataService;
//    }
//
//    /*public JWTResponse loginWithJWT(UserLoginBO userLoginBO) {
//        String jwtToken = userLoginDataService.getTokenAndRefreshCache(userLoginBO);
//        return JWTResponse.newBuilder().success(true).accessToken(jwtToken).build();
//    }*/
//
//    public void afterLogin(Long userId) {
//        userTaskRecordProvider.createOrUpdate(userId, TaskEnum.SIGN_IN.getId());//创建签到
//        userLoginLogProvider.create(userId, true, "");//记录登录
//        //userMetricsProvider.updateLoginDays(userId);//更新登录
//    }
//
//}