package com.xxx.auth.controller.bo.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookCodeRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String state;

    @NotBlank
    private String redirectUri; // 必须和前端发起授权时一致

    private String referralCode;

}
