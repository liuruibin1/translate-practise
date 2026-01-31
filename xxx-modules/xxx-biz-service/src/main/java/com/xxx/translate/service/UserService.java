package com.xxx.translate.service;

import com.xxx.common.core.base.service.BaseServiceImpl;
import com.xxx.entity.User;
import com.xxx.translate.mapper.UserMapper;
import com.xxx.translate.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseServiceImpl<User, UserVO, UserMapper> {

    final UserMapper mapper;

    public UserService(UserMapper mapper) {
        this.mapper = mapper;
    }

}
