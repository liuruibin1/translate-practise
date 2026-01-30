package com.xxx.auth.controller.bo.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterUserData {

    private String id;

    private String username;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

}