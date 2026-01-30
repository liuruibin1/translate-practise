package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRoundMessage {
    private Long id;
    private Long number;
    private Integer roomId;
    private Integer gameId;
    private Integer status;
    private Long deadlineMs;
    private String drawResult;
    private String message;
}