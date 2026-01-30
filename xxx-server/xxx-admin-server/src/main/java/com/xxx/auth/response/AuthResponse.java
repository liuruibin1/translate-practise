package com.xxx.auth.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthResponse implements Serializable {
    private Boolean success;
    private String message;
    private Integer code;
    private String accessToken;
    private String refreshToken;
    private Long expiresTs;
}
