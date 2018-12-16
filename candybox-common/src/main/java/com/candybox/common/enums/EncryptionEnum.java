package com.candybox.common.enums;


import sun.misc.BASE64Encoder;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
* Copyright (c) 2018. slst.com all rights reserved
* @Description 加密
* @author  Homvee.Tang
* @date 2018-07-17 16:56
* @version V1.0
*/
public enum EncryptionEnum {


    /**
     * 将MD5的结果转为Base64
     * MD5加密后的值是128bit的，将结果用Base64 编码
     */
    MD5_2_BASE64(){
        @Override
        public String encrypt(String data) throws Exception {
            byte[] encrypted = EncryptionEnum.getMD5(data);
            return BASE_64_ENCODER.encode(encrypted);
        }

    },
    /**
     * 将MD5的结果转为16进制数据
     * MD5加密后的值是128bit的，按4位二进制组合成一个十六进制，所以最后出来的十六进制字符串是32个
     */
    MD5_2_HEX(){
        @Override
        public String encrypt(String data) throws Exception {
            byte[] encrypted = EncryptionEnum.getMD5(data);
            return new BigInteger(encrypted).toString(16);
        }
    }

    ;


    private static byte[] getMD5(String data) throws Exception {
        String in = data + "";
        byte[] bytes = in.getBytes("UTF-8");
        MessageDigest md5=MessageDigest.getInstance("MD5");
        /**
         * MD5的结果是一个16字节128位数组
         */
        byte[] encrypted = md5.digest(bytes);
        return encrypted;
    }


   private static final BASE64Encoder BASE_64_ENCODER = new BASE64Encoder();

   public abstract String encrypt(String data) throws Exception;
}
