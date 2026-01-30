package com.xxx.base.bo;

import java.math.BigDecimal;

public interface IGameOrder {

    Long getUserId();

    Integer getGameId();

    Long getGameRoundId();

    Integer getGameBetTypeId();

    Integer getCurrencyId();

    Integer getBetCount();

    BigDecimal getBetAmount();

    Long getCreateTsMs();

    String getGameRoundDrawResult();

    Integer getGameRoundNum1();
    Integer getGameRoundNum2();

    Integer getGameRoundF1();
    Integer getGameRoundF2();

}