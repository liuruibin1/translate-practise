package com.xxx.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.controller.BaseController;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.request.PageQuery;
import com.xxx.base.vo.Response;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.system.provider.ISysDeptProvider;
import com.xxx.system.utils.SysDeptUtil;
import com.xxx.system.vo.SysDeptVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sys/sys-dept")
public class SysDeptController extends BaseController {

    @DubboReference
    ISysDeptProvider sysDeptProvider;

    @PreAuthorize("hasAuthority('sys_dept:view')")
    @PostMapping("/page")
    @ResponseBody
    public IPage<SysDeptVO> page(@RequestBody PageQuery<SysDeptVO> pageQuery) {
        Page<SysDeptVO> pageParam = getPage(pageQuery);
        return this.sysDeptProvider.queryPage(pageParam, pageQuery.getDtoParam());
    }

    @PreAuthorize("hasAuthority('sys_dept:view')")
    @PostMapping("/tree")
    @ResponseBody
    public List<SysDeptVO> tree(@RequestBody SysDeptVO dtoParam) {
        SysDeptVO voParam = dtoParamToVOParam(dtoParam, SysDeptVO.class);
        List<SysDeptVO> voList = sysDeptProvider.queryList(voParam);
        return SysDeptUtil.buildTree(voList);
    }

    @PreAuthorize("hasAuthority('sys_dept:save')")
    @PostMapping("/save")
    @ResponseBody
    public Response save(HttpServletRequest request, @RequestBody SysDeptVO dtoParam) {
        SysDeptVO voParam = dtoParamToVOParam(dtoParam, SysDeptVO.class);
        if (ObjectUtils.isNotNull(voParam.getId())) {
            return sysDeptProvider.modifyById(
                    voParam,
                    OperatorTypeEnum.SYSTEM,
                    getSysUserId(request));
        } else {
            return sysDeptProvider.create(
                    voParam,
                    OperatorTypeEnum.SYSTEM,
                    getSysUserId(request));
        }
    }

    @PreAuthorize("hasAuthority('sys_dept:delete')")
    @GetMapping("/delete")
    @ResponseBody
    public Response delete(HttpServletRequest request, @RequestParam Integer id) {
        return sysDeptProvider.deleteById(
                id,
                OperatorTypeEnum.SYSTEM,
                getSysUserId(request));
    }

}