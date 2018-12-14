package com.slst.common.utils;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/5/30 12:08
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String valStr){
        if (null != valStr && valStr.trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

}
