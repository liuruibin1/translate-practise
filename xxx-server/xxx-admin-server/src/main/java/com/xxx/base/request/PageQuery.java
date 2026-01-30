package com.xxx.base.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery<T>  implements Serializable {

    private T dtoParam;
    private Long current;
    private Long size;

}