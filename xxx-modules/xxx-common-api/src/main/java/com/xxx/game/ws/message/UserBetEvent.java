package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBetEvent {
    private Integer gameBetTypeId;
    private Integer gameBetOptionId;
    private BigDecimal betAmount;
}