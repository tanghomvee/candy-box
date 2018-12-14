package com.slst.common.enums;

/**
 * @Description 交易类型A枚举 对应accountRecord表的typea
 * 顶级交易类型 1.充值,2.充入,3.收入,4.消费,5.回收,6.退回,7.划出,8.划入,9.冻结,10.解冻,11.提现,12.扣除
 */
public enum TransTypeAEnum {

    /**
     * 充值
     */
    CHARGING(1,"充值"),

    /**
     * 充入
     */
    CHARGE_IN(2,"入账"),

    /**
     * 收入
     */
    INCOME(3,"收入"),

    /**
     * 消费
     */
    COST(4,"消费"),

    /**
     * 回收
     */
    RETRIEVE(5,"回收"),

    /**
     * 退回
     */
    RETREAT(6,"退回"),

    /**
     * 划出
     */
    PAYOUT(7,"划出"),

    /**
     * 划入
     */
    GET_IN(8,"划入"),

    /**
     * 冻结
     */
    FROZEN(9,"冻结"),

    /**
     * 解冻
     */
    UNFROZEN(10,"解冻"),

    /**
     * 提现
     */
    WITHDRAW(11,"提现"),

    /**
     * 扣除
     */
    WITHHOLD(12,"扣除");

    private Integer val;
    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    TransTypeAEnum(Integer val,String desc){
        this.val=val;
        this.desc=desc;
    }

    public static TransTypeAEnum getByVal(Integer val){
        for (TransTypeAEnum transTypeAEnum : TransTypeAEnum.values()) {
            if (transTypeAEnum.val.equals(val)){
                return  transTypeAEnum;
            }
        }

        return  null;
    }
}
