package com.xxx.auth.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaCodeResponse implements Serializable {
    private Boolean success;
    private String message;
    private String uuid;
    private String img;
}
