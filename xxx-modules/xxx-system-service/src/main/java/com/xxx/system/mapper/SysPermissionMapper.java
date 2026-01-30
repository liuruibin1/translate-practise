package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.system.entity.SysPermission;
import com.xxx.system.vo.SysPermissionVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermissionVO> queryList(@Param("paramObj") SysPermissionVO paramObj);

    @Select("SELECT MAX(id) FROM sys_permission")
    Integer getMaxId();

    @Select("<script>" +
            "SELECT DISTINCT(sp_.code) AS code" +
            " FROM sys_permission sp_" +
            "         LEFT JOIN sys_role_permission srp_ ON sp_.id = srp_.permission_id" +
            "         LEFT JOIN sys_user_role sur_ ON srp_.role_id = sur_.role_id" +
            " WHERE sp_.code IS NOT NULL" +
            "    <if test='sysUserId != null'>" +
            "        AND sur_.sys_user_id = #{sysUserId}" +
            "    </if>" +
            "  AND sp_.is_enabled = TRUE" +
            "</script>")
    List<String> queryDistinctCodeBySysUserIdIsEnabledTrue(@Param("sysUserId") Long sysUserId);

    @Select("<script>" +
            "SELECT * FROM sys_permission sp" +
            " WHERE sp.id IN (" +
            "    SELECT DISTINCT (sp_.id)" +
            "    FROM sys_permission sp_" +
            "           LEFT JOIN sys_role_permission srp_ ON sp_.id = srp_.permission_id" +
            "           LEFT JOIN sys_user_role sur_ ON srp_.role_id = sur_.role_id" +
            "    WHERE sp_.is_enabled=TRUE " +
            "    <if test='sysUserId != null'>" +
            "       AND sur_.sys_user_id = #{sysUserId}" +
            "    </if> )" +
            " ORDER BY sp.sort" +
            "</script>")
    List<SysPermissionVO> queryBySysUserIdIsEnabledTrue(@Param("sysUserId") Long sysUserId);

    @Update("UPDATE sys_permission" +
            " SET is_visible=#{isVisible}" +
            ",update_ts=#{updateTs}" +
            " WHERE id=#{id}")
    int updateIsVisibleById(
            @Param("isVisible") Boolean isVisible,
            @Param("updateTs") Date updateTs,
            @Param("id") Integer id);

    @Update("UPDATE sys_permission" +
            " SET is_cache=#{isCache}" +
            ",update_ts=#{updateTs}" +
            " WHERE id=#{id}")
    int updateIsCacheById(
            @Param("isCache") Boolean isCache,
            @Param("updateTs") Date updateTs,
            @Param("id") Integer id);

    @Update("UPDATE sys_permission" +
            " SET is_enabled=#{isEnabled}" +
            ",update_ts=#{updateTs}" +
            " WHERE id=#{id}")
    int updateIsEnabledById(
            @Param("isEnabled") Boolean isEnabled,
            @Param("updateTs") Date updateTs,
            @Param("id") Integer id);

}