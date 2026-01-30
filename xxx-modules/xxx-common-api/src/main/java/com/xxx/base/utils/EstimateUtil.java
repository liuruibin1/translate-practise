package com.xxx.base.utils;

import com.xxx.base.enumerate.EstimateTypeEnum;

import java.math.BigDecimal;

public class EstimateUtil {

    public static boolean isEvaluate(
            Integer estimateType,
            BigDecimal estimateBetAmount,
            BigDecimal estimateProfitRatio,
            Integer estimateProfitRatioCount,
            BigDecimal estimateLooseningOneIssueAmount,
            BigDecimal turnOffEstimateLooseningDailyLossAmount) {
        if (estimateType.equals(EstimateTypeEnum.ESTIMATE_PROFIT_CLOSEST.getValue())  //估算利润取最接近
                || estimateType.equals(EstimateTypeEnum.ESTIMATE_PROFIT_RANDOM.getValue())) { //估算利润取随机
            if (estimateBetAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            if (estimateProfitRatio.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
            if (estimateProfitRatioCount != null && estimateProfitRatioCount.equals(0)) {
                return false;
            }
            return true;
        } else if (estimateType.equals(EstimateTypeEnum.ESTIMATE_ISSUE_LOOSENING.getValue()) //估算单期放水
                || estimateType.equals(EstimateTypeEnum.ESTIMATE_ISSUE_LOOSENING_ID_ODD.getValue())
                || estimateType.equals(EstimateTypeEnum.ESTIMATE_ISSUE_LOOSENING_ID_EVEN.getValue())) {
            if (estimateLooseningOneIssueAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            if (turnOffEstimateLooseningDailyLossAmount.compareTo(BigDecimal.ZERO) >= 0) {
                return false;
            }
            return true;
        }
        return false;
    }

}