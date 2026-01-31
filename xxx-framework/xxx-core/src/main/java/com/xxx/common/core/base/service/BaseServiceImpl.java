package com.xxx.common.core.base.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.core.base.vo.Response;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import static com.xxx.common.core.enumerate.BizErrorEnum.*;

public abstract class BaseServiceImpl<Entity, VO extends Entity, Mapper extends BaseMapper<Entity>> extends ServiceImpl<Mapper, Entity> {

    public IPage<VO> queryPage(Page<VO> page, VO voParam) {
        return null;
    }

    public List<VO> queryList(VO voParam) {
        return null;
    }

    public VO queryOne(VO voParam) {
        return null;
    }

    public Long queryCount(VO voParam) {
        return null;
    }

    protected void buildParamObj(VO voParam) {
    }

    protected void buildReturn(List<VO> voList, VO voParam) {
    }

    protected Integer getNewIdAsInteger() {
        return null;
    }

    protected Long getNewIdAsLong() {
        return null;
    }

    protected Response validationField(Entity entity, boolean isCreate) {
        return Response.success();
    }

    public List<Entity> queryByIdList(List<? extends Serializable> idList) {
        if (!idList.isEmpty()) {
            return this.listByIds(idList);
        } else {
            return null;
        }
    }

    public Entity queryById(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    public Response create(Entity entity) {
        return Response.error(_550_001);
    }

    public Response deleteById(Serializable id) {
        return Response.error(_550_002);
    }

    public Response modifyById(Entity entity) {
        return Response.error(_550_003);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response createImpl(Entity entity) {
        if (this.baseMapper.insert(entity) > 0) {
            return Response.success();
        } else {
            return Response.error(_551_001);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteByIdImpl(Serializable id) {
        if (this.baseMapper.deleteById(id) > 0) {
            return Response.success();
        } else {
            return Response.error(_551_002);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modifyByIdImpl(Entity entity) {
        if (this.baseMapper.updateById(entity) > 0) {
            return Response.success();
        } else {
            return Response.error(_551_003);
        }
    }

}
