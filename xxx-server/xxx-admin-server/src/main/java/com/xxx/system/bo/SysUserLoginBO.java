package com.xxx.system.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xxx.system.vo.SysUserVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserLoginBO implements Serializable {

    private String jwtToken;
    private Long loginTimestamp;
    private Long expireTimestamp;

    // JsonIgnoreProperties 这里用VO
    private SysUserVO userData;
    private List<Integer> roleIdList;
    private List<String> permissionCodeList;

}