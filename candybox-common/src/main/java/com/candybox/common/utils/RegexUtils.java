package com.candybox.common.utils;

import java.util.regex.Pattern;

public class RegexUtils {

    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private static final String REGEX_NUMBER ="^[-\\+]?[\\d]*$";

    public static boolean isMobile(String mobile) {
        if (11!=mobile.length()){
            return false;
        }

        return Pattern.matches(REGEX_NUMBER, mobile);
    }

}
