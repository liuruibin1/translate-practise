package com.xxx.auth.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String username;
    private String password;
    private String captchaCode;
    private String captchaCodeUuid;
}
