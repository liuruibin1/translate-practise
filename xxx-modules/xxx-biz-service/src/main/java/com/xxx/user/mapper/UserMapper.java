package com.xxx.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.user.entity.User;
import com.xxx.user.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    IPage<UserVO> queryPage(@Param("page") Page<UserVO> page, @Param("paramObj") UserVO paramObj);

    List<UserVO> queryList(@Param("paramObj") UserVO paramObj);

    UserVO queryOne(@Param("paramObj") UserVO paramObj);

    @Select("SELECT MAX(id) FROM user")
    Long getMaxId();

}