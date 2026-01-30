package com.xxx.base.utils;

import com.xxx.base.constants.OddsConstant;
import com.xxx.base.vo.Response;

import java.math.BigDecimal;

import static com.xxx.common.core.enumerate.BizErrorEnum.*;

public class OddsUtil {

    public static Response verifyGtBeforeOdds(
            BigDecimal odds,
            BigDecimal odds2,
            BigDecimal odds3,
            BigDecimal odds4,
            BigDecimal odds5,
            BigDecimal odds6,
            BigDecimal odds7,
            BigDecimal odds8,
            BigDecimal oddsSpecial) {
        if (odds == null) {
            return Response.error(_34_001);
        }
        if (OddsConstant.MIN_ODDS.compareTo(odds) > 0
                || (odds2 != null && OddsConstant.MIN_ODDS.compareTo(odds2) > 0)
                || (odds3 != null && OddsConstant.MIN_ODDS.compareTo(odds3) > 0)
                || (odds4 != null && OddsConstant.MIN_ODDS.compareTo(odds4) > 0)
                || (odds5 != null && OddsConstant.MIN_ODDS.compareTo(odds5) > 0)
                || (odds6 != null && OddsConstant.MIN_ODDS.compareTo(odds6) > 0)
                || (odds7 != null && OddsConstant.MIN_ODDS.compareTo(odds7) > 0)
                || (odds8 != null && OddsConstant.MIN_ODDS.compareTo(odds8) > 0)
                || (oddsSpecial != null && OddsConstant.MIN_ODDS.compareTo(oddsSpecial) > 0)) {
            return Response.error(_34_001);
        }
        if (OddsConstant.MAX_ODDS.compareTo(odds) < 0
                || (odds2 != null && OddsConstant.MAX_ODDS.compareTo(odds2) < 0)
                || (odds3 != null && OddsConstant.MAX_ODDS.compareTo(odds3) < 0)
                || (odds4 != null && OddsConstant.MAX_ODDS.compareTo(odds4) < 0)
                || (odds5 != null && OddsConstant.MAX_ODDS.compareTo(odds5) < 0)
                || (odds6 != null && OddsConstant.MAX_ODDS.compareTo(odds6) < 0)
                || (odds7 != null && OddsConstant.MAX_ODDS.compareTo(odds7) < 0)
                || (odds8 != null && OddsConstant.MAX_ODDS.compareTo(odds8) < 0)
                || (oddsSpecial != null && OddsConstant.MAX_ODDS.compareTo(oddsSpecial) < 0)) {
            return Response.error(_34_001);
        }
        Response response = verifyGtBeforeOdds(odds, odds2);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyGtBeforeOdds(odds2, odds3);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyGtBeforeOdds(odds3, odds4);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyGtBeforeOdds(odds4, odds5);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyGtBeforeOdds(odds5, odds6);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyGtBeforeOdds(odds6, odds7);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyGtBeforeOdds(odds7, odds8);
        if (!response.isSuccess()) {
            return response;
        }
        //特殊赔率 验证
        response = verifyLtBeforeOdds(odds, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyLtBeforeOdds(odds2, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyLtBeforeOdds(odds3, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyLtBeforeOdds(odds4, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyLtBeforeOdds(odds5, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyLtBeforeOdds(odds6, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        response = verifyLtBeforeOdds(odds7, oddsSpecial);
        if (!response.isSuccess()) {
            return response;
        }
        return verifyLtBeforeOdds(odds8, oddsSpecial);
    }

    /**
     * 后面的要大于前面的赔率
     */
    private static Response verifyGtBeforeOdds(BigDecimal oddsA, BigDecimal oddsB) {
        if (oddsA != null && oddsB != null && oddsA.compareTo(oddsB) > 0) {
            return Response.error(_34_003);
        }
        return Response.success();
    }

    /**
     * 后面的要小于前面的赔率
     */
    private static Response verifyLtBeforeOdds(BigDecimal oddsA, BigDecimal oddsB) {
        if (oddsA != null && oddsB != null && oddsB.compareTo(oddsA) > 0) {
            return Response.error(_34_004);
        }
        return Response.success();
    }

}