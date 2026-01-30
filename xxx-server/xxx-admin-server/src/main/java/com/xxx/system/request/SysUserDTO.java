package com.xxx.system.request;

import lombok.Data;

import java.util.List;

@Data
public class SysUserDTO {
    private List<String> userCacheKeyList;
}