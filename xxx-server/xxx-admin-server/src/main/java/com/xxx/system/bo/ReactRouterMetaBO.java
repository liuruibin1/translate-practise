package com.xxx.system.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReactRouterMetaBO implements Serializable {
    private String title;

    private String icon;

    private Boolean noCache;

    private String link;
}