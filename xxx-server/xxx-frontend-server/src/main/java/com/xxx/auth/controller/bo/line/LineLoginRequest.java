package com.xxx.auth.controller.bo.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineLoginRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String state;

    private String referralCode;

}
