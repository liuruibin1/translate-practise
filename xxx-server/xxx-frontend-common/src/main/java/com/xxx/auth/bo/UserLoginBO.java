package com.xxx.auth.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xxx.user.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginBO implements Serializable {

    private String jwtToken;
    private Long loginTimestamp;
    private Long expireTimestamp;

    private User user;

}