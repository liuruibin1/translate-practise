package com.xxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.system.entity.SysDept;
import com.xxx.system.vo.SysDeptVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {

    IPage<SysDeptVO> queryPage(@Param("page") Page<SysDeptVO> page, @Param("paramObj") SysDeptVO paramObj);

    List<SysDeptVO> queryList(@Param("paramObj") SysDeptVO paramObj);

    @Select("SELECT MAX(id) FROM sys_dept")
    Integer getMaxId();

}