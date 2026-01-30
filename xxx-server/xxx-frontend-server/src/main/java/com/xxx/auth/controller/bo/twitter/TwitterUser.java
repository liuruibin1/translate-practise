package com.xxx.auth.controller.bo.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterUser {

    private TwitterUserData data;

}