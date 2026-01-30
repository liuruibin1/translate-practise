package com.xxx.common.core.utils;

import com.xxx.common.core.constants.SpringProfilesConstant;

public class SpringProfilesUtils {

    public static boolean isProduction(String springProfilesActive) {
        return SpringProfilesConstant.PROD.equals(springProfilesActive);
    }

    public static boolean isTest(String springProfilesActive) {
        return SpringProfilesConstant.TEST.equals(springProfilesActive);
    }

}
