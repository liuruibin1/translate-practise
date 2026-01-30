package com.xxx.common.core.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsDailyBetDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ExcelProperty("统计日期")
    @ColumnWidth(15)
    private String statisticsDate;

    @ExcelProperty("预计投注数额")
    @ColumnWidth(15)
    private String betAmount;

    @ExcelProperty("实际投注数额")
    @ColumnWidth(15)
    private String actualBetAmount;

    @ExcelProperty("有效投注数额")
    @ColumnWidth(15)
    private String effectiveBetAmount;

    @ExcelProperty("预计投注注数")
    @ColumnWidth(10)
    private Long betCount;

    @ExcelProperty("实际投注注数")
    @ColumnWidth(10)
    private Long actualBetCount;

    @ExcelProperty("预计返奖")
    @ColumnWidth(15)
    private String prizeAmount;

    @ExcelProperty("实际返奖")
    @ColumnWidth(15)
    private String actualPrizeAmount;

    @ExcelProperty("预计返奖注数")
    @ColumnWidth(10)
    private Long prizeCount;

    @ExcelProperty("平台盈亏")
    @ColumnWidth(15)
    private String profitOrLoss;


    @ExcelProperty("预计投注数额计价后")
    @ColumnWidth(15)
    private String betAmountQV;

    @ExcelProperty("实际投注数额计价后")
    @ColumnWidth(15)
    private String actualBetAmountQV;

    @ExcelProperty("有效投注数额计价后")
    @ColumnWidth(15)
    private String effectiveBetAmountQV;

    @ExcelProperty("预计返奖数额计价后")
    @ColumnWidth(15)
    private String prizeAmountQV;

    @ExcelProperty("实际返奖数额计价后")
    @ColumnWidth(15)
    private String actualPrizeAmountQV;

    @ExcelProperty("平台盈亏计价后")
    @ColumnWidth(15)
    private String profitOrLossQV;


    @ExcelProperty("订单数量")
    @ColumnWidth(10)
    private Long countUserGameOrder;

    @ExcelProperty("投注人数")
    @ColumnWidth(10)
    private Long countUserGameOrderUserId;
}
