package com.xxx.auth.controller.bo.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterLoginRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String state;

    private String referralCode;

}
