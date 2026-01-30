package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.system.entity.SysRolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    @Select("SELECT DISTINCT(permission_id) AS permission_id" +
            " FROM sys_role_permission" +
            " WHERE role_id=#{roleId}")
    List<Integer> queryDistinctPermissionIdByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT MAX(id) FROM sys_role_permission")
    Integer getMaxId();

    @Delete("DELETE FROM sys_role_permission WHERE role_id=#{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    //@Delete("DELETE FROM sys_role_permission WHERE permission_id=#{permissionId}")
    //int deleteByPermissionId(@Param("permissionId") Integer permissionId);

}