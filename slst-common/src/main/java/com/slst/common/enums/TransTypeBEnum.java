package com.slst.common.enums;

/**
 * @Description 交易类型A枚举 对应accountRecord表的typeb
 * 顶级交易类型 1.现金,2.线上,3.电话匹配,4.短信(消费,收入,冻结)
 */
public enum TransTypeBEnum {

    /**
     * 细分交易类型:现金
     */
    CASH(1,"现金"),
    /**
     * 细分交易类型:线上
     */
    ONLINE(2,"线上"),
    /**
     * 细分交易类型:电话匹配消费
     */
    PHONE_MATCH(3,"电话匹配"),
    /**
     * 细分交易类型:短信消费,收入,冻结,解冻
     */
    SMS(4,"短信"),

    TAG_MATCH(5,"标签匹配"),

    /**
     * 细分交易类型:打电话消费,收入,冻结,解冻
     */
    PHONE_CALL(6,"打电话");



    private Integer val;
    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    TransTypeBEnum(Integer val,String desc){
        this.val=val;
        this.desc=desc;
    }

    public static TransTypeBEnum getByVal(Integer val){
        for (TransTypeBEnum transTypeBEnum : TransTypeBEnum.values()) {
            if (transTypeBEnum.val.equals(val)){
                return  transTypeBEnum;
            }
        }

        return  null;
    }
}
