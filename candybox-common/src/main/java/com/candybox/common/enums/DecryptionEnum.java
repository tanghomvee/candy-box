package com.candybox.common.enums;


public enum DecryptionEnum {

    MD5(){

        @Override
        String decrypt(String data) {
            return null;
        }
    };


    abstract String decrypt(String data);
}
