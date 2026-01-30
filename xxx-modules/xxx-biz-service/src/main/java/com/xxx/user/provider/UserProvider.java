package com.xxx.user.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.user.entity.User;
import com.xxx.user.enumerate.UserTypeEnum;
import com.xxx.user.service.UserService;
import com.xxx.user.vo.UserVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@DubboService
public class UserProvider implements IUserProvider {

    final UserService service;

    public UserProvider(UserService service) {
        this.service = service;
    }

    @Override
    public IPage<UserVO> queryPage(Page<UserVO> pageParam, UserVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<UserVO> queryList(UserVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public UserVO queryOne(UserVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(UserVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<User> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public User queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(User entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(User entity) {
        return service.modifyById(entity);
    }

}