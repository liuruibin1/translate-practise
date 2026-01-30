package com.xxx.user.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.provider.IProvider;
import com.xxx.base.vo.Response;
import com.xxx.user.entity.User;
import com.xxx.user.enumerate.UserTypeEnum;
import com.xxx.user.vo.UserVO;

import java.math.BigDecimal;
import java.util.List;

public interface IUserProvider extends IProvider<User, UserVO> {

//    Response create(
//            Long currentMerchantUserId,
//            Long assignParentId,
//            String username,
//            String password,
//            UserTypeEnum userTypeEnum,
//            Boolean isTest,
//            OperatorTypeEnum operatorTypeEnum,
//            Long operatorId);
//
//
//    Response bindTelegramById(
//            Long id,
//            Long telegramId,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response bindEmailById(
//            Long id,
//            String email,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response bindPhoneNumberById(
//            Long id,
//            String phoneNumber,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response updateUsername(
//            Long id,
//            String username,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//
//    Response updatePasswordById(
//            Long currentMerchantUserId,
//            Long id,
//            String password,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response updatePasswordById(
//            Long id,
//            String password,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response updateWithdrawalPasswordById(
//            Long id,
//            String withdrawalPassword,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response updateReceiveMessagesTelegramIdsById(
//            Long currentMerchantUserId,
//            Long id,
//            String receiveMessagesTelegramIds,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    Response batchUpdateIsEnabledById(
//            Long currentMerchantUserId,
//            List<Long> idList,
//            Boolean isEnabled,
//            OperatorTypeEnum operatorType,
//            Long operatorId);
//
//    User queryOneByUsername(String username);
//
//
//    String obtainUsername(String baseUsername);
//
//    Long countByReferrerId(Long referrerId);

}
