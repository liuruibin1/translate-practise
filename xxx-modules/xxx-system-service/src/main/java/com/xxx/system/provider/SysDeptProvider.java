package com.xxx.system.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.vo.Response;
import com.xxx.system.entity.SysDept;
import com.xxx.system.service.SysDeptService;
import com.xxx.system.vo.SysDeptVO;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.Serializable;
import java.util.List;

@DubboService
public class SysDeptProvider implements ISysDeptProvider {

    final SysDeptService service;

    public SysDeptProvider(SysDeptService service) {
        this.service = service;
    }

    @Override
    public IPage<SysDeptVO> queryPage(Page<SysDeptVO> pageParam, SysDeptVO voParam) {
        return service.queryPage(pageParam, voParam);
    }

    @Override
    public List<SysDeptVO> queryList(SysDeptVO voParam) {
        return service.queryList(voParam);
    }

    @Override
    public SysDeptVO queryOne(SysDeptVO voParam) {
        return service.queryOne(voParam);
    }

    @Override
    public Long queryCount(SysDeptVO voParam) {
        return service.queryCount(voParam);
    }

    @Override
    public List<SysDept> queryByIdList(List<? extends Serializable> idList) {
        return service.queryByIdList(idList);
    }

    @Override
    public SysDept queryById(Serializable id) {
        return service.queryById(id);
    }

    @Override
    public Response create(SysDept entity) {
        return service.create(entity);
    }

    @Override
    public Response deleteById(Serializable id) {
        return service.deleteById(id);
    }

    @Override
    public Response modifyById(SysDept entity) {
        return service.modifyById(entity);
    }

    @Override
    public Response create(SysDeptVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        return service.create(vo, operatorType, operatorId);
    }

    @Override
    public Response modifyById(SysDeptVO vo, OperatorTypeEnum operatorType, Long operatorId) {
        return service.modifyById(vo, operatorType, operatorId);
    }

    @Override
    public Response deleteById(Integer id, OperatorTypeEnum operatorType, Long operatorId) {
        return service.deleteById(id, operatorType, operatorId);
    }

}