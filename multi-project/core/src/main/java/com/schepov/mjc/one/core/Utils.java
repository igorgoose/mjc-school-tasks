package com.schepov.mjc.one.core;

import com.schepov.mjc.one.build.StringUtils;

public class Utils {

    private Utils(){

    }

    public static boolean isAllPositiveNumbers(String... strings) {
        for (String str : strings) {
            if (!StringUtils.isPositiveNumber(str)) {
                return false;
            }
        }
        return true;
    }

}
