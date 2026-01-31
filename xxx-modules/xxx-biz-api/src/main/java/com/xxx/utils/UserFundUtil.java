package com.xxx.utils;

import com.xxx.common.core.utils.uuid.UUIDUtil;

public class UserFundUtil {

    public static String buildId(Long userId, Integer currencyId) {
        return UUIDUtil.generate32ByLowerCaseSeedArray(String.valueOf(userId), currencyId.toString());
    }

}
