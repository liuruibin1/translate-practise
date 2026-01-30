package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.system.entity.SysUser;
import com.xxx.system.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUserVO> queryPage(@Param("page") Page<SysUserVO> page, @Param("paramObj") SysUserVO paramObj);

    List<SysUserVO> queryList(@Param("paramObj") SysUserVO paramObj);

    SysUserVO queryOne(@Param("paramObj") SysUserVO paramObj);

    @Select("SELECT MAX(id) FROM sys_user")
    Long getMaxId();

    @Update("UPDATE sys_user SET password=#{password},update_ts=#{updateTs} WHERE id=#{id}")
    int updatePasswordById(
            @Param("password") String password,
            @Param("updateTs") Date updateTs,
            @Param("id") Long id);

    @Update("UPDATE sys_user SET is_enabled=#{isEnabled},update_ts=#{updateTs} WHERE id=#{id}")
    int updateIsEnabledById(
            @Param("isEnabled") Boolean isEnabled,
            @Param("updateTs") Date updateTs,
            @Param("id") Long id);

    @Update("UPDATE sys_user SET is_deleted=#{isDeleted},update_ts=#{updateTs} WHERE id=#{id}")
    int updateIsDeletedById(
            @Param("isDeleted") Boolean isDeleted,
            @Param("updateTs") Date updateTs,
            @Param("id") Long id);

}