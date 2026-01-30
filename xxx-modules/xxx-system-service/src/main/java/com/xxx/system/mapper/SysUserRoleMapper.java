package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("SELECT MAX(id) FROM sys_user_role")
    Integer getMaxId();

    @Select("<script>" +
            " SELECT * FROM sys_user_role WHERE sys_user_id IN" +
            "   <foreach item='item' index='index' collection='sysUserIdList' open='(' separator=',' close=')'>" +
            "       #{item}" +
            "   </foreach>" +
            "</script>")
    List<SysUserRole> queryListBySysUserIdList(@Param("sysUserIdList") List<Long> sysUserIdList);

    @Delete("DELETE FROM sys_user_role WHERE sys_user_id=#{sysUserId}")
    int deleteBySysUserId(@Param("sysUserId") Serializable sysUserId);

    //@Delete("DELETE FROM sys_user_role WHERE user_id=#{userId}")
    //int deleteByUserId(@Param("userId") Serializable userId);

    @Delete("DELETE FROM sys_user_role WHERE role_id=#{roleId}")
    int deleteByRoleId(@Param("roleId") Serializable roleId);

}
