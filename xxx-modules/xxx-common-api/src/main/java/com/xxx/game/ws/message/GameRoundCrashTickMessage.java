package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRoundCrashTickMessage {
    private Integer roomId;
    private Long gameRoundId;
    private Boolean crashed;
    private Long elapsed;
    private Double multiplier;
}