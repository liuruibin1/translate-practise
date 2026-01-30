package com.xxx.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.javafaker.Faker;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.common.core.exception.BusinessRuntimeException;
import com.xxx.common.core.utils.DateMillisUtils;
import com.xxx.common.core.utils.DateUtils;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.user.entity.User;
import com.xxx.user.mapper.UserMapper;
import com.xxx.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseServiceImpl<User, UserVO, UserMapper> {

    final UserMapper mapper;

    public UserService(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public IPage<UserVO> queryPage(Page<UserVO> pageParam, UserVO voParam) {
        buildParamObj(voParam);
        return mapper.queryPage(pageParam, voParam);
    }

    @Override
    public List<UserVO> queryList(UserVO voParam) {
        buildParamObj(voParam);
        return mapper.queryList(voParam);
    }

    @Override
    public UserVO queryOne(UserVO voParam) {
        buildParamObj(voParam);
        return mapper.queryOne(voParam);
    }

    @Override
    protected void buildParamObj(UserVO voParam) {
        //注册时间
        String createDateStrGE = voParam.getCreateTsMsStrGE();
        if (createDateStrGE != null) {
            if (DateUtils.isValidDateStr(createDateStrGE, DateUtils.REGEX_yyyy_MM_dd)) {
                long msGE = DateUtils.dateStrToMillis(createDateStrGE);
                voParam.setCreateTsMsGE(msGE);
            }
        }
        String createDateStrLE = voParam.getCreateTsMsStrLE();
        if (createDateStrLE != null) {
            if (DateUtils.isValidDateStr(createDateStrLE, DateUtils.REGEX_yyyy_MM_dd)) {
                long msLE = DateUtils.dateStrToMillis(createDateStrLE) + DateMillisUtils.DAY_MILLIS - 1;
                voParam.setCreateTsMsLE(msLE);
            }
        }
    }

    public User queryOneByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return this.getOne(queryWrapper);
    }

    public User queryOneByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return this.getOne(queryWrapper);
    }

    public User queryOneByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhoneNumber, phoneNumber);
        return this.getOne(queryWrapper);
    }

    public User queryOneByTwitterId(String twitterId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getTwitterId, twitterId);
        return this.getOne(queryWrapper);
    }

    public User queryOneByFacebookId(String facebookId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getFacebookId, facebookId);
        return this.getOne(queryWrapper);
    }

    public User queryOneByLineId(String lineId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getLineId, lineId);
        return this.getOne(queryWrapper);
    }

    public List<User> queryByType(Integer type) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getType, type);
        return this.list(queryWrapper);
    }

    public User queryOneByCryptoAccountChainIdCryptoAccountAddress(Integer chainId, String chainAddress) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getChainId, chainId)
                .eq(User::getChainAddress, chainAddress);
        return this.getOne(queryWrapper);
    }

    public User queryOneByTelegramId(Long telegramId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getTelegramId, telegramId);
        return this.getOne(queryWrapper);
    }

    public User queryOneByReferralCode(String referralCode) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getReferralCode, referralCode);
        return this.getOne(queryWrapper);
    }

    /**
     * 获取不存在数据库的用户名
     */
    public String obtainUsername(String baseUsername) {
        Faker faker = new Faker();
        String newUsername;
        if (StringUtils.isNotEmpty(baseUsername)) {
            newUsername = baseUsername + faker.number().numberBetween(100, 999);
        } else {
            newUsername = faker.name().firstName() + faker.name().lastName();
        }
        User dbEntity = queryOneByUsername(newUsername);
        int count = 0;
        while (ObjectUtils.isNotNull(dbEntity) && count < 30) {//30次退出，直接异常
            newUsername = faker.name().firstName() + faker.name().lastName();
            dbEntity = queryOneByUsername(newUsername);
            count++;
        }
        if (ObjectUtils.isNotNull(dbEntity)) {
            throw new BusinessRuntimeException("User already exists");
        }
        return newUsername;
    }

    public Long countByReferrerId(Long referrerId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getReferrerId, referrerId);
        return this.count(queryWrapper);
    }


}