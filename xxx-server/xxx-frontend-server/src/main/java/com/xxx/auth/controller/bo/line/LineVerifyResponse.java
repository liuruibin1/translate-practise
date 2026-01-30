package com.xxx.auth.controller.bo.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineVerifyResponse {

    private String sub;

}
