package com.xxx.translate.provider;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.common.core.base.vo.Response;
import com.xxx.entity.User;
import com.xxx.translate.service.UserService;
import com.xxx.translate.vo.UserVO;

@Service
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
