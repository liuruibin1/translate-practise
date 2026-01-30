package com.xxx.auth.controller.bo.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterTokenResponse {

    private String token_type;
    private long expires_in;
    private String access_token;
    private String refresh_token;
    private String scope;

}