package com.xxx.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.common.core.utils.BeanUtil;
import com.xxx.common.core.utils.ObjectUtils;

public abstract class AbsController {

    protected <QueryArgs, Entity> Entity queryArgsToObj(Class<Entity> distObjClass) {
        return objToNewObj(null, distObjClass, true);
    }

    protected <E> Page<E> queryArgsToPage(Integer currentPage, Integer pageSize) {
        Page<E> page = new Page<>(1, 10);
        if (ObjectUtils.isNotNull(pageSize)) {
            page.setSize(pageSize);
        }
        if (ObjectUtils.isNotNull(currentPage)) {
            page.setCurrent(currentPage);
        }
        return page;
    }

    protected <SrcObj, DistObj> DistObj objToNewObj(SrcObj srcObj, Class<DistObj> distObjClazz) {
        return objToNewObj(srcObj, distObjClazz, false);
    }

    protected <SrcObj, DistObj> DistObj objToNewObj(SrcObj srcObj, Class<DistObj> distObjClazz, boolean isNewInstance) {
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

    //    protected <E, F> List<F> objListToNewObjList(List<E> eList, Class<F> clazz) {
    //        if (ObjectUtils.isNull(eList)) {
    //            return null;
    //        }
    //        return BeanUtil.copyToNewList(eList, clazz);
    //    }
    //

    //    protected JWTResponse buildJWTResponse(
    //            Long idValue,
    //            String idClaimKey,
    //            String secret,
    //            String issuer,
    //            Long tokenValidDuration
    //    ) {
    //        Map<String, Object> claimMap = new HashMap<>();
    //        claimMap.put(idClaimKey, idValue);
    //        claimMap.put(USER_TYPE, OperatorTypeEnum.USER.getValue());
    //        String jwtToken = JWTUtil.buildToken(claimMap,
    //                secret,
    //                issuer,
    //                tokenValidDuration,
    //                TimeUnit.MINUTES);
    //        JWTResponse jwtResponse = new JWTResponse();
    //        jwtResponse.setSuccess(true);
    //        jwtResponse.setAccessToken(jwtToken);
    //        return jwtResponse;
    //    }

}