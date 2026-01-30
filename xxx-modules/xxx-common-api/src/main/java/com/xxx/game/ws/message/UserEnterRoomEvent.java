package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEnterRoomEvent {
    private Integer roomId;
}