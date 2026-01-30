package com.xxx.user.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.controller.BaseController;
import com.xxx.base.dto.IdListOfLongIsEnabledDTO;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.request.PageQuery;
import com.xxx.base.vo.Response;
import com.xxx.common.core.dto.UserDTO;
import com.xxx.common.core.utils.BigDecimalUtil;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.system.bo.SysUserLoginBO;
import com.xxx.system.entity.SysUser;
import com.xxx.system.provider.ISysUserProvider;
import com.xxx.user.entity.User;
import com.xxx.user.enumerate.UserTypeEnum;
import com.xxx.user.provider.IUserProvider;
import com.xxx.user.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xxx.common.core.enumerate.BizErrorEnum._10_001;
import static com.xxx.common.core.enumerate.BizErrorEnum._10_002;

@Controller
@RequestMapping("/user/user")
public class UserController extends BaseController {

    @DubboReference
    IUserProvider userProvider;

    @DubboReference
    ISysUserProvider sysUserProvider;

    final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('user:view')")
    @PostMapping("/page")
    @ResponseBody
    public IPage<UserVO> page(HttpServletRequest request, @RequestBody PageQuery<UserVO> pageQuery) {
        Page<UserVO> pageParam = getPage(pageQuery);
        UserVO voParam = dtoParamToVOParam(pageQuery.getDtoParam(), UserVO.class);
        voParam.setCurrentMerchantUserId(getCurrentMerchantUserId(request));
        return this.userProvider.queryPage(pageParam, voParam);
    }

    @PreAuthorize("hasAuthority('user:view')")
    @PostMapping("/query-one")
    @ResponseBody
    public UserVO queryOne(@RequestBody UserVO dtoParam) {
        UserVO voParam = dtoParamToVOParam(dtoParam, UserVO.class);
        return this.userProvider.queryOne(voParam);
    }

    @PreAuthorize("hasAuthority('user:view')")
    @PostMapping("/tree")
    @ResponseBody
    public List<UserVO> tree(HttpServletRequest request, @RequestBody UserVO dtoParam) {
        SysUserLoginBO sysUserLoginBO = getLoginData(request);
        if (sysUserLoginBO == null) {
            return null;
        }
        UserVO voParam = dtoParamToVOParam(dtoParam, UserVO.class);
        if (voParam.getCurrentMerchantUserId() == null) {
            voParam.setCurrentMerchantUserId(sysUserLoginBO.getUserData().getMerchantUserId());
        }
        List<UserVO> voList = this.userProvider.queryList(voParam);
        return buildUserTree(voList);
    }

//    @PreAuthorize("hasAuthority('user:view')")
//    @GetMapping("/username-exists")
//    @ResponseBody
//    public Response usernameExists(@RequestParam String username) {
//        SysUser sysUser = sysUserProvider.queryOneByUsername(username);
//        User user = userProvider.queryOneByUsername(username);
//        if (sysUser == null && user == null) {
//            return Response.success();
//        }
//        return Response.error(_10_002);
//    }

//    @PreAuthorize("hasAuthority('user:save')")
//    @GetMapping("/create")
//    @ResponseBody
//    public Response create(HttpServletRequest request,
//                           @RequestParam Long assignParentId,
//                           @RequestParam String username,
//                           @RequestParam(required = false) String password,
//                           @RequestParam Integer type,
//                           @RequestParam Boolean isTest) {
//        Long currentMerchantUserId = getCurrentMerchantUserId(request);//当前商户ID
//        UserTypeEnum userTypeEnum = UserTypeEnum.getByType(type);
//        if (userTypeEnum == null) {
//            return Response.error("User type non-existent");
//        }
//        if (UserTypeEnum.MERCHANT.getType().equals(type)) { //商户 密码不能为空
//            if (StringUtils.isEmpty(password) && password.length() < 6) {
//                return Response.error("Password invalid, length must between 6 and 32");
//            }
//        }
//        Response response = userProvider.create(
//                currentMerchantUserId,
//                assignParentId,
//                username,
//                password,
//                userTypeEnum,
//                isTest,
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        if (!response.isSuccess()) {
//            return response;
//        }
//        User dbUser = (User) response.getData();
//        if (dbUser == null) {
//            return Response.error(_10_001);
//        }
//        if (userTypeEnum.equals(UserTypeEnum.MERCHANT)) {//创建商户
//            response = sysUserProvider.createForMerchant(
//                    username,
//                    passwordEncoder.encode(password),
//                    dbUser.getId(),
//                    OperatorTypeEnum.SYSTEM,
//                    getSysUserId(request));
//        }
//        return response;
//    }
//
//    @PreAuthorize("hasAuthority('user:save')")
//    @PostMapping("/batch-update-is-enabled")
//    @ResponseBody
//    public Response batchUpdateIsEnabled(HttpServletRequest request, @RequestBody IdListOfLongIsEnabledDTO dtoParam) {
//        Long currentMerchantUserId = getCurrentMerchantUserId(request);//当前商户ID
//        Response response = userProvider.batchUpdateIsEnabledById(
//                currentMerchantUserId,
//                dtoParam.getIdList(),
//                dtoParam.getIsEnabled(),
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        if (!response.isSuccess()) {
//            return response;
//        }
//        response = sysUserProvider.batchUpdateIsEnabledByMerchantUserIdList(
//                dtoParam.getIsEnabled(),
//                dtoParam.getIdList(),
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//        return response;
//    }
//
//    @PreAuthorize("hasAuthority('user:save')")
//    @GetMapping("/update-user-password")
//    @ResponseBody
//    public Response updateUserPassword(HttpServletRequest request,
//                                       @RequestParam Long userId,
//                                       @RequestParam String newPassword) {
//        Long currentMerchantUserId = getCurrentMerchantUserId(request);//当前商户ID
//        Response response = userProvider.updatePasswordById(
//                currentMerchantUserId,
//                userId != null ? userId : currentMerchantUserId,
//                newPassword,
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//
//        User user = userProvider.queryById(userId);
//        if (user.getType().equals(UserTypeEnum.MERCHANT.getType())) {
//            response = sysUserProvider.updatePasswordByMerchantUserId(
//                    passwordEncoder.encode(newPassword),
//                    user.getId(),
//                    OperatorTypeEnum.SYSTEM,
//                    getSysUserId(request));
//        }
//        return response;
//    }
//
//    @PreAuthorize("hasAuthority('user:save')")
//    @GetMapping("/update-receive-messages-telegram-ids")
//    @ResponseBody
//    public Response updateReceiveMessagesTelegramIds(HttpServletRequest request,
//                                                     @RequestParam Long userId,
//                                                     @RequestParam String receiveMessagesTelegramIds) {
//        Long currentMerchantUserId = getCurrentMerchantUserId(request);//当前商户ID
//        return userProvider.updateReceiveMessagesTelegramIdsById(
//                currentMerchantUserId,
//                userId,
//                receiveMessagesTelegramIds,
//                OperatorTypeEnum.SYSTEM,
//                getSysUserId(request));
//    }
//
//    @PreAuthorize("hasAuthority('user:export')")
//    @PostMapping("/export")
//    @ResponseBody
//    public void exportBigData(HttpServletResponse response, HttpServletRequest request, @RequestBody PageQuery<UserVO> pageQuery) {
//        try {
//            // 设置响应头
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setCharacterEncoding("utf-8");
//            String fileName = URLEncoder.encode("用户彩票订单导出", StandardCharsets.UTF_8);
//            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//            if (pageQuery.getSize() > 2000L) {
//                pageQuery.setSize(2000L);
//            }
//            Page<UserVO> pageParam = getPage(pageQuery);
//            UserVO voParam = dtoParamToVOParam(pageQuery.getDtoParam(), UserVO.class);
//            voParam.setCurrentMerchantUserId(getCurrentMerchantUserId(request));
//            IPage<UserVO> userVOIPage = this.userProvider.queryPage(pageParam, voParam);
//
//            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), UserDTO.class)
//                    .excelType(ExcelTypeEnum.XLSX)
//                    .build();
//            WriteSheet writeSheet = EasyExcel.writerSheet("用户列表").build();
//            List<UserVO> records = userVOIPage.getRecords();
//            List<UserDTO> list = new ArrayList<>();
//            for (UserVO userVO : records) {
//                UserDTO userDTO = new UserDTO();
//                userDTO.setId(userVO.getId());
//                userDTO.setEmail(userVO.getEmail());
//                userDTO.setPhoneNumber(userVO.getPhoneNumber());
//                userDTO.setReferralCode(userVO.getReferralCode());
//                userDTO.setVipLevel(userVO.getVipLevel());
//                UserGameOrderVO vo = userProvider.queryOneWithSumUserGameOrderByUserId(userVO.getQuoteCurrencyId(), userVO.getId());
//                //总盈亏
//                BigDecimal profitOrLoss =
//                        vo.getActualPrizeAmountQV().subtract(vo.getEffectiveBetAmountQV());
//                userDTO.setProfitOrLoss(
//                        BigDecimalUtil.formatAmountStr(profitOrLoss)
//                                + userVO.getQuoteCurrencySymbol()
//                                + userVO.getQuoteCurrencyCode()
//                );
//                userDTO.setAvailableBalance(userVO.getAvailableBalanceQV() + userVO.getQuoteCurrencySymbol() + userVO.getQuoteCurrencyCode());
//                userDTO.setAvailableBalance(
//                        BigDecimalUtil.formatAmountStr(userVO.getAvailableBalanceQV())
//                                + userVO.getQuoteCurrencySymbol()
//                                + userVO.getQuoteCurrencyCode()
//                );
//
//                userDTO.setTotalBalance(
//                        BigDecimalUtil.formatAmountStr(userVO.getTotalBalanceQV())
//                                + userVO.getQuoteCurrencySymbol()
//                                + userVO.getQuoteCurrencyCode()
//                );
//                userDTO.setCreateTsMs(userVO.getCreateTsMs());
//                list.add(userDTO);
//            }
//            excelWriter.write(list, writeSheet);
//            excelWriter.finish();
//
//        } catch (IOException e) {
//            throw new RuntimeException("导出失败", e);
//        }
//    }

    private List<UserVO> buildUserTree(List<UserVO> voList) {
        Map<Long, UserVO> nodeMap = new HashMap<>();
        for (UserVO vo : voList) {
            nodeMap.put(vo.getId(), vo);
        }
        List<UserVO> roots = new ArrayList<>();
        for (User user : voList) {
            UserVO currentNode = nodeMap.get(user.getId());
            Long parentId = user.getParentId();
            if (parentId == null) {
                roots.add(currentNode);
            } else {
                UserVO parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(currentNode);
                } else {
                    roots.add(currentNode);
                }
            }
        }
        return roots;
    }

}