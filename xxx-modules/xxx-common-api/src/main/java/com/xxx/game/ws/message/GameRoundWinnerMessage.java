package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRoundWinnerMessage {
    private Integer roomId;
    private Long gameRoundId;
    private String drawResult;
    private Integer f1;
    private Integer f2;
    private List<Winner> winnerList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Winner {
        private Long userId;

        private Integer currencyId;
        private String currencyCode;
        private String currencySymbol;
        private Integer currencyType;
        private BigDecimal prizeAmount;

        private Integer quoteCurrencyId;
        private String quoteCurrencyCode;
        private String quoteCurrencySymbol;
        private Integer quoteCurrencyType;
        private BigDecimal prizeAmountQV;
    }

}