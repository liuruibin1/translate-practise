package com.xxx.base.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService
public class XXXAdminServerProvider extends BaseServerInfoProvider implements IXXXAdminServerProvider {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Override
    public String getSpringApplicationName() {
        return springApplicationName;
    }

}