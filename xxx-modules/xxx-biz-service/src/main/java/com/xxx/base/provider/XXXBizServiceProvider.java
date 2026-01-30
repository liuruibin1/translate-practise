package com.xxx.base.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService
public class XXXBizServiceProvider extends BaseServerInfoProvider implements IXXXBizServiceProvider {

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Override
    public String getSpringApplicationName() {
        return springApplicationName;
    }

}