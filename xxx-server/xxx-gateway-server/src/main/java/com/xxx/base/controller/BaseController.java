package com.xxx.base.controller;

import com.xxx.base.provider.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/base") // 建议使用特定前缀区分网关本地端点
public class BaseController {

    @DubboReference
    IXXXAdminServerMerchantProvider xxxAdminServerMerchantProvider;

    @DubboReference
    IXXXAdminServerProvider xxxAdminServerProvider;

    @DubboReference
    IXXXBizServiceProvider xxxBizServiceProvider;

    @DubboReference
    IXXXFrontendServerProvider xxxFrontendServerProvider;

    @DubboReference
    IXXXFrontendWsProvider xxxFrontendWsProvider;

    @DubboReference
    IXXXGatewayServerProvider xxxGatewayServerProvider;

    @DubboReference
    IXXXJobExecutorProvider xxxJobExecutorProvider;

    @DubboReference
    IXXXSystemServiceProvider xxxSystemServiceProvider;

    @GetMapping("/serverInfo")
    @ResponseBody
    public List<Map<String, Object>> serverInfo() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (isAvailable(xxxAdminServerMerchantProvider)) {
            mapList.add(xxxAdminServerMerchantProvider.serverInfo());
        }
        if (isAvailable(xxxAdminServerProvider)) {
            mapList.add(xxxAdminServerProvider.serverInfo());
        }
        if (isAvailable(xxxBizServiceProvider)) {
            mapList.add(xxxBizServiceProvider.serverInfo());
        }
        if (isAvailable(xxxFrontendServerProvider)) {
            mapList.add(xxxFrontendServerProvider.serverInfo());
        }
        if (isAvailable(xxxFrontendWsProvider)) {
            mapList.add(xxxFrontendWsProvider.serverInfo());
        }
        if (isAvailable(xxxGatewayServerProvider)) {
            mapList.add(xxxGatewayServerProvider.serverInfo());
        }
        if (isAvailable(xxxJobExecutorProvider)) {
            mapList.add(xxxJobExecutorProvider.serverInfo());
        }
        if (isAvailable(xxxSystemServiceProvider)) {
            mapList.add(xxxSystemServiceProvider.serverInfo());
        }
        mapList.sort(Comparator.comparing(m -> ((String) m.get("appName"))));
        return mapList;
    }

    private boolean isAvailable(Object provider) {
        try {
            //EchoService echoService = (EchoService) provider;
            //Object ok = echoService.$echo("OK");
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

}