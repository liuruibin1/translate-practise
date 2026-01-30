package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBetMessage implements Serializable {
    private Long userId;
    private String username;
    private Integer gameBetTypeId;
    private Integer gameBetOptionId;
    private Integer betCurrencyId;
    private BigDecimal betAmount;
    private Integer quoteCurrencyId;
    private BigDecimal betAmountQV;
}