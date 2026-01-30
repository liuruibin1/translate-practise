package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 * "decoded": {
 *   "type": "text_comment",
 *   "comment": "1985597651"
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Decoded implements Serializable {

    private String type;

    private String comment;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
