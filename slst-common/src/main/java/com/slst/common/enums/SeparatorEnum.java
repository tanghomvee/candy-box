package com.slst.common.enums;


/**
 * Created by ddyunf on 2017/9/29.
 */
public enum SeparatorEnum {
    /**
     * 下划线
     */
    UNDERLINE("_","下划线"),


    /**
     * 空格
     */
    SPACE(" ","空格"),

    /**
     * =
     */
    EQUAL("=","等号"),
    /**
     * &
     */
    AND("&","与"),

    /**
     * 逗号
     */
    COMMA(",","逗号"),
    /**
     * 斜杠
     */
    SLASH(",","斜杠"),

    /**
     * 冒号
     */
    COLON(":","冒号")
    ;


    private String val;
    private String desc;

    SeparatorEnum(String value, String desc ){
        this.val=value;
        this.desc=desc;
    }

    public  static SeparatorEnum getByVal(String val){
        for (SeparatorEnum tmp : SeparatorEnum.values()){
            if(tmp.val.equals(val)){
                return tmp;
            }
        }
        return null;
    }

    public String getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}
