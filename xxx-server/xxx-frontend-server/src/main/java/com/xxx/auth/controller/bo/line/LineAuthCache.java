package com.xxx.auth.controller.bo.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineAuthCache implements Serializable {

    private String state;

    private String nonce;

    private String codeVerifier;

    private long createdAtMs;

}
