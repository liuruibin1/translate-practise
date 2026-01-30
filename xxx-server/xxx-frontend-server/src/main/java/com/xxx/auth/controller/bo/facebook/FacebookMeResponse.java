package com.xxx.auth.controller.bo.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookMeResponse {

    private String id;
    private String name;
    private String email;

    private Picture picture;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Picture {
        private DataNode data;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DataNode {
            private String url;

            @JsonProperty("is_silhouette")
            private Boolean isSilhouette;
        }
    }

}
