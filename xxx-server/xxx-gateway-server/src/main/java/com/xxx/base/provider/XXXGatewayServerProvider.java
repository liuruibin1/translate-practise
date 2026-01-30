package com.xxx.base.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService
public class XXXGatewayServerProvider extends BaseServerInfoProvider implements IXXXGatewayServerProvider {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Override
    public String getSpringApplicationName() {
        return springApplicationName;
    }

}