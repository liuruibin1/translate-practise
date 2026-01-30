package com.xxx.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.auth.service.SysUserOnlineService;
import com.xxx.base.controller.BaseController;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.request.PageQuery;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.system.bo.SysUserLoginBO;
import com.xxx.system.entity.SysUserRole;
import com.xxx.system.provider.ISysPermissionProvider;
import com.xxx.system.provider.ISysUserProvider;
import com.xxx.system.provider.ISysUserRoleProvider;
import com.xxx.system.request.SysUserDTO;
import com.xxx.system.vo.SysUserVO;
import com.xxx.user.provider.IUserProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/sys-user")
public class SysUserController extends BaseController {

    final PasswordEncoder passwordEncoder;

    final SysUserOnlineService sysUserOnlineService;

    @DubboReference
    ISysUserProvider sysUserProvider;

    @DubboReference
    ISysUserRoleProvider sysUserRoleProvider;

    @DubboReference
    ISysPermissionProvider sysPermissionProvider;

    @DubboReference
    IUserProvider userProvider;

    public SysUserController(PasswordEncoder passwordEncoder, SysUserOnlineService sysUserOnlineService) {
        this.passwordEncoder = passwordEncoder;
        this.sysUserOnlineService = sysUserOnlineService;
    }

    @PreAuthorize("hasAuthority('sys_user:view')")
    @PostMapping("/page")
    @ResponseBody
    public IPage<SysUserVO> page(@RequestBody PageQuery<SysUserVO> pageQuery) {
        Page<SysUserVO> pageParam = getPage(pageQuery);
        IPage<SysUserVO> page = this.sysUserProvider.queryPage(pageParam, pageQuery.getDtoParam());
        setRoleIdList(page.getRecords());
        return page;
    }

    @PreAuthorize("hasAuthority('sys_user:view')")
    @PostMapping("/list")
    @ResponseBody
    public List<SysUserVO> list(@RequestBody SysUserVO dtoParam) {
        return this.sysUserProvider.queryList(dtoParam);
    }

    @GetMapping("/info")
    @ResponseBody
    public SysUserVO info(HttpServletRequest request) {
        Long sysUserId = getSysUserId(request);
        if (sysUserId == null) {
            return null;
        }
        SysUserVO voParam = new SysUserVO();
        voParam.setId(sysUserId);
        SysUserVO retVO = this.sysUserProvider.queryOne(voParam);
        List<String> permissionCodeList = sysPermissionProvider.queryDistinctCodeBySysUserIdIsEnabledTrue(voParam.getId());
        retVO.setPermissionCodeList(permissionCodeList);
        return retVO;
    }

    @PreAuthorize("hasAuthority('sys_user:save')")
    @PostMapping("/save")
    @ResponseBody
    public Response save(HttpServletRequest request, @RequestBody SysUserVO dtoParam) {
        SysUserVO voParam = dtoParamToVOParam(dtoParam, SysUserVO.class);
        if (ObjectUtils.isNotNull(voParam.getId())) {
            return sysUserProvider.modifyById(
                    voParam,
                    OperatorTypeEnum.SYSTEM,
                    getSysUserId(request));
        } else {
            String encodePassword = passwordEncoder.encode(voParam.getPassword());
            voParam.setPassword(encodePassword);
            return sysUserProvider.create(
                    voParam,
                    OperatorTypeEnum.SYSTEM,
                    getSysUserId(request));
        }
    }

    @PreAuthorize("hasAuthority('sys_user:delete')")
    @GetMapping("/delete")
    @ResponseBody
    public Response delete(HttpServletRequest request, @RequestParam Long id) {
        return sysUserProvider.deleteById(
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

//    @PreAuthorize("hasAuthority('sys_user:resetPassword')")
//    @GetMapping("/reset-password")
//    @ResponseBody
//    public Response resetPassword(HttpServletRequest request,
//                                  @RequestParam String newPassword,
//                                  @RequestParam Long id) {
//        String encodePassword = passwordEncoder.encode(newPassword);
//        Response response = sysUserProvider.updatePasswordById(
//                encodePassword,
//                id,
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        if (!response.isSuccess()) {
//            return response;
//        }
//        SysUser sysUser = sysUserProvider.queryById(id);
//        if (sysUser != null && sysUser.getMerchantUserId() != null) {
//            response = userProvider.updatePasswordById(   //修改商户
//                    UserConstant.USER_ADMIN_ID, //指定 最高级商户
//                    sysUser.getMerchantUserId(),
//                    newPassword,
//                    OperatorTypeEnum.SYSTEM,
//                    getSysUserId(request));
//        }
//        return response;
//    }

//    @PreAuthorize("hasAuthority('sys_user:updateIsEnabled')")
//    @GetMapping("/update-is-enabled")
//    @ResponseBody
//    public Response updateIsEnabled(HttpServletRequest request,
//                                    @RequestParam Boolean isEnabled,
//                                    @RequestParam Long id) {
//        Response response = sysUserProvider.updateIsEnabledById(
//                isEnabled,
//                id,
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        if (!response.isSuccess()) {
//            return response;
//        }
//        SysUser sysUser = sysUserProvider.queryById(id);
//        if (sysUser != null && sysUser.getMerchantUserId() != null) {
//            response = userProvider.batchUpdateIsEnabledById(//修改商户
//                    UserConstant.USER_ADMIN_ID, //指定 最高级商户
//                    List.of(sysUser.getMerchantUserId()),
//                    isEnabled,
//                    OperatorTypeEnum.SYSTEM,
//                    getSysUserId(request));
//        }
//        return response;
//    }

    @PreAuthorize("hasAuthority('sys_user:updateIsDeleted')")
    @GetMapping("/update-is-deleted")
    @ResponseBody
    public Response updateIsDeleted(HttpServletRequest request,
                                    @RequestParam Boolean isDeleted,
                                    @RequestParam Long id) {
        return sysUserProvider.updateIsDeletedById(
                isDeleted,
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

    @PostMapping("/update-info")
    @ResponseBody
    public Response updateInfo(HttpServletRequest request, @RequestBody SysUserVO dtoParam) {
        Long currentSysUserId = getSysUserId(request);
        SysUserVO voParam = dtoParamToVOParam(dtoParam, SysUserVO.class);
        voParam.setId(currentSysUserId);
        return sysUserProvider.modifyById(voParam);
    }

//    @GetMapping("/update-password")
//    @ResponseBody
//    public Response updatePassword(HttpServletRequest request,
//                                   @RequestParam String oldPassword,
//                                   @RequestParam String newPassword,
//                                   @RequestParam String confirmNewPassword) {
//        //验证密码
//        if (!newPassword.equals(confirmNewPassword)) {
//            return Response.error("The two passwords don't match");
//        }
//        //
//        SysUser dbSysUser = sysUserProvider.queryById(getSysUserId(request));
//        if (!passwordEncoder.matches(oldPassword, dbSysUser.getPassword())) {
//            return Response.error("The old password is incorrect");
//        }
//        if (passwordEncoder.matches(newPassword, dbSysUser.getPassword())) {
//            return Response.error("The new password must be different from the original password");
//        }
//        Response response = sysUserProvider.updatePasswordById(
//                passwordEncoder.encode(newPassword),
//                dbSysUser.getId(),
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        if (!response.isSuccess()) {
//            return Response.error(response.getMessage());
//        }
//        if (dbSysUser.getMerchantUserId() != null) {
//            response = userProvider.updatePasswordById( //修改商户
//                    UserConstant.USER_ADMIN_ID, //指定 最高级商户
//                    dbSysUser.getMerchantUserId(),
//                    newPassword,
//                    OperatorTypeEnum.SYSTEM,
//                    getSysUserId(request));
//        }
//        if (!response.isSuccess()) {
//            return Response.error(response.getMessage());
//        }
//        //强制登出
//        sysUserOnlineService.forceLogoutByUserId(dbSysUser.getId());
//        return Response.success();
//    }

//    @GetMapping("/forget-password")
//    @ResponseBody
//    public Response forgetPassword(HttpServletRequest request,
//                                   @RequestParam String username,
//                                   @RequestParam String email,
//                                   @RequestParam String newPassword,
//                                   @RequestParam String confirmNewPassword,
//                                   @RequestParam String verificationCode) {
//        SysUser dbSysUser = sysUserProvider.queryByUsernameEmail(username, email);
//        //验证用户
//        if (ObjectUtils.isNull(dbSysUser)) {
//            return Response.error("System user non-existent");
//        }
//        //验证码验证
//        BaseResponse response = checkVerificationCode(
//                verificationCode,
//                VerificationCodeTypeEnum.SYS_USER_FORGET_PASSWORD,
//                DeliveryMethodEnum.EMAIL,
//                dbSysUser.getEmail());
//        if (!response.isSuccess()) {
//            return Response.error(response.getMessage());
//        }
//        //验证密码
//        if (!newPassword.equals(confirmNewPassword)) {
//            return Response.error("The two passwords don't match");
//        }
//        if (passwordEncoder.matches(newPassword, dbSysUser.getPassword())) {
//            return Response.error("The new password must be different from the original password");
//        }
//        response = sysUserProvider.updatePasswordById(
//                passwordEncoder.encode(newPassword),
//                dbSysUser.getId(),
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        if (!response.isSuccess()) {
//            return Response.error(response.getMessage());
//        }
//        //强制登出
//        sysUserOnlineService.forceLogoutByUserId(dbSysUser.getId());
//        return Response.success(true);
//    }

    @PreAuthorize("hasAuthority('sys_user:view')")
    @GetMapping("/list-online")
    @ResponseBody
    public List<SysUserLoginBO> listOnline() {
        //Collection<String> keys = redisService.scan100(SysUserCacheConstant.SYS_USER_LOGGED_DATA);
        //List<SysUserLoginBO> sysUserLoginBOList = new ArrayList<>();
        //for (String key : keys) {
        //    Object sysUserLoggedDataCacheObj = redisService.opsForValueAndGet(key);
        //    SysUserLoginBO sysUserLoginBO = JSONUtil.objectToObject(sysUserLoggedDataCacheObj, SysUserLoginBO.class);
        //    sysUserLoginBOList.add(sysUserLoginBO);
        //}
        //List<SysUserLoginBO> loginUserFList = new ArrayList<>();
        //for (SysUserLoginBO sysUserLoginBO : sysUserLoginBOList) {
        //    SysUserLoginBO userLoginBO = new SysUserLoginBO();
        //    userLoginBO.setLoginTimestamp(sysUserLoginBO.getLoginTimestamp());
        //    userLoginBO.setExpireTimestamp(sysUserLoginBO.getExpireTimestamp());
        //    userLoginBO.setPermissionCodeList(sysUserLoginBO.getPermissionCodeList());
        //    userLoginBO.setRoleIdList(sysUserLoginBO.getRoleIdList());
        //    userLoginBO.setUserData(sysUserLoginBO.getUserData());
        //    loginUserFList.add(userLoginBO);
        //}
        //return sysUserLoginBOList;
        return null;
    }

    @PreAuthorize("hasAuthority('sys_user:forceLogout')")
    @PostMapping("/force-logout")
    @ResponseBody
    public Response forceLogout(@RequestBody SysUserDTO dtoParam) {
        for (String userCacheKey : dtoParam.getUserCacheKeyList()) {
            redisService.delete(SysUserCacheConstant.SYS_USER_LOGGED_DATA + userCacheKey);
        }
        return Response.success();
    }

    private void setRoleIdList(List<SysUserVO> sysUserVOList) {
        if (sysUserVOList.isEmpty()) {
            return;
        }
        List<Long> sysUserIdList = sysUserVOList.stream().map(SysUserVO::getId).toList();
        if (sysUserIdList.isEmpty()) {
            return;
        }
        List<SysUserRole> sysUserRoleList = sysUserRoleProvider.queryListBySysUserIdList(sysUserIdList);
        if (sysUserRoleList.isEmpty()) {
            return;
        }
        for (SysUserVO sysUserVO : sysUserVOList) {
            List<Integer> roleIdList = sysUserRoleList
                    .stream()
                    .filter(i -> i.getSysUserId().equals(sysUserVO.getId()))
                    .map(SysUserRole::getRoleId)
                    .distinct()
                    .toList();
            sysUserVO.setRoleIdList(roleIdList);
        }
    }

}