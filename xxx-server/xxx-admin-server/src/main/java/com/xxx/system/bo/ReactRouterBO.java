package com.xxx.system.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReactRouterBO implements Serializable {
    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private String query;

    private Boolean alwaysShow;

    private String code;

    private ReactRouterMetaBO meta;

    private List<ReactRouterBO> children;

}