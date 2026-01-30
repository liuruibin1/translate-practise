package com.xxx.base.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.vo.Response;

import java.io.Serializable;
import java.util.List;

public interface IProvider<Entity, VO> {

    IPage<VO> queryPage(Page<VO> pageParam, VO voParam);

    List<VO> queryList(VO voParam);

    VO queryOne(VO voParam);

    Long queryCount(VO voParam);

    List<Entity> queryByIdList(List<? extends Serializable> idList);

    Entity queryById(Serializable id);

    Response create(Entity entity);

    Response deleteById(Serializable id);

    Response modifyById(Entity entity);

}
