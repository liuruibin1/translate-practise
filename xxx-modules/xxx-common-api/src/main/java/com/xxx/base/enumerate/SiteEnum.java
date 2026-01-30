package com.xxx.base.enumerate;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum SiteEnum implements Serializable {

    INFURA_IO("infura.io"), //官网 https://infura.io
    ALCHEMY_COM("alchemy.com"), //https://alchemy.com

    ;

    private final String url;

    SiteEnum(String url) {
        this.url = url;
    }

}