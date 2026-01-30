package com.xxx.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.request.PageQuery;
import com.xxx.common.core.utils.BeanUtil;
import com.xxx.common.core.utils.ObjectUtils;

public class AbsController {

    protected <DTO, VO> VO dtoParamToVOParam(DTO dto, Class<VO> voClass) {
        return objToNewObj(dto, voClass, true);
    }

    protected <E, T> Page<E> getPage(PageQuery<T> pageQuery) {
        Page<E> page = new Page<>(1, 10);
        if (ObjectUtils.isNull(pageQuery)) {
            return page;
        } else {
            if (ObjectUtils.isNotNull(pageQuery.getSize())) {
                page.setSize(pageQuery.getSize());
            }
            if (ObjectUtils.isNotNull(pageQuery.getCurrent())) {
                page.setCurrent(pageQuery.getCurrent());
            }
        }
        return page;
    }

    private  <SrcObj, DistObj> DistObj objToNewObj(SrcObj srcObj, Class<DistObj> distObjClazz, boolean isNewInstance) {
        if (ObjectUtils.isNull(srcObj)) {
            if (isNewInstance) {
                try {
                    return distObjClazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return BeanUtil.copyToNewBean(srcObj, distObjClazz);
    }

}