package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.system.entity.SysRole;
import com.xxx.system.vo.SysRoleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    IPage<SysRoleVO> queryPage(@Param("page") Page<SysRoleVO> page, @Param("paramObj") SysRoleVO paramObj);

    List<SysRoleVO> queryList(@Param("paramObj") SysRoleVO paramObj);

    @Select("SELECT MAX(id) FROM sys_role")
    Integer getMaxId();

    @Select("<script>" +
            "SELECT DISTINCT(sr_.id) AS id" +
            " FROM sys_role sr_" +
            "         LEFT JOIN sys_user_role sur_ ON sr_.id = sur_.role_id" +
            " WHERE sr_.is_enabled = true" +
            "    <if test='sysUserId != null'>" +
            "        AND sur_.sys_user_id = #{sysUserId}" +
            "    </if>" +
            "</script>")
    List<Integer> queryDistinctIdBySysUserIdIsEnabledTrue(@Param("sysUserId") Long sysUserId);

}