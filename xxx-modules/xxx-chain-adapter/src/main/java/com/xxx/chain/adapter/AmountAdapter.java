package com.xxx.chain.adapter;


import com.xxx.common.core.utils.BigDecimalUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountAdapter {

    /**
     * 数额 乘 精度次幂
     *
     * @param decimals 小数位
     * @param amount   数额
     * @return
     */
    public static BigDecimal decimalAmountToRaw(Integer decimals, BigDecimal amount) {
        BigDecimal power = BigDecimalUtil.get10Power(decimals);
        return amount.multiply(power);
    }

    /**
     * 数额 除 精度次幂
     *
     * @param amount   数额
     * @param decimals 小数位
     * @return
     */
    public static BigDecimal rawAmountToDecimal(BigDecimal amount, Integer decimals) {
        BigDecimal power = BigDecimalUtil.get10Power(decimals);
        return amount.divide(power, decimals, RoundingMode.DOWN);
    }

}
