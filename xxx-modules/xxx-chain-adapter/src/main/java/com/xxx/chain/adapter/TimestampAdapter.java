package com.xxx.chain.adapter;


import com.xxx.chain.enumerate.ChainTypeEnum;

public class TimestampAdapter {

    //    public static Long getBeforeBlockTimestamp(Long currentBlockTimestamp, int beforeSeconds, ChainTypeEnum chainType) {
    //        if (currentBlockTimestamp != null
    //                && currentBlockTimestamp > 0
    //                && beforeSeconds > 0) {
    //            if (chainType.equals(ChainTypeEnum.ETHEREUM) || chainType.equals(ChainTypeEnum.BTC)) {
    //                if (currentBlockTimestamp > beforeSeconds) {
    //                    return currentBlockTimestamp - beforeSeconds;
    //                }
    //            }
    //        }
    //        return 0L;
    //    }

    public static Long toUTCMillisecond(Long txTimestamp, ChainTypeEnum chainType) {
        if (txTimestamp != null && txTimestamp > 0) {
            if (chainType.equals(ChainTypeEnum.ETHEREUM) || chainType.equals(ChainTypeEnum.BTC)) {
                return txTimestamp * 1000;
            } else if (chainType.equals(ChainTypeEnum.TRON)) {
                return txTimestamp;
            } else {
                return txTimestamp;
            }
        }
        return 0L;
    }

}