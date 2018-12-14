package com.slst.acct.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "t_account_record")
public class AccountRecord extends BaseEntity {
    /**金额*/
    private Long amount;
    /**交易基础类型:1.充值,2.入账,3.收入,4.消费,5.回收,6.退回,7.划出,8.划入,9.冻结,10.解冻,11.提现,12.扣除*/
    private Integer typea;
    /**交易细分类型*/
    private Integer typeb;
    /**交易时间*/
    private Date time;
    /**交易备注*/
    private String remark;
//    /**金额来源0：来源不明，1：代理商划入金额，2：线上充值金额*/
//    private Integer origin;
    /**本账户ID*/
    private Long acctId;
    /**交易账户ID:用于查看支出或收入来源账户*/
    private Long tradeAcctId;
    /**当前账户对应的用户ID*/
    private Long userId;
    /**门店ID。如果为0则不是门店消费记录，如果大于0则是门店消费记录*/
    private Long storeId;

    public Long getStoreId(){
        return storeId;
    }

    public void setStoreId(Long storeId){
        this.storeId = storeId;
    }


    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }


    public Long getTradeAcctId(){
        return tradeAcctId;
    }

    public void setTradeAcctId(Long tradeAcctId){
        this.tradeAcctId = tradeAcctId;
    }


    public Long getAcctId(){
        return acctId;
    }

    public void setAcctId(Long acctId){
        this.acctId = acctId;
    }


//    public Integer getOrigin(){
//        return origin;
//    }
//
//    public void setOrigin(Integer origin){
//        this.origin = origin;
//    }


    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }


    public Date getTime(){
        return time;
    }

    public void setTime(Date time){
        this.time = time;
    }


    public Integer getTypeb(){
        return typeb;
    }

    public void setTypeb(Integer typeb){
        this.typeb = typeb;
    }


    public Integer getTypea(){
        return typea;
    }

    public void setTypea(Integer typea){
        this.typea = typea;
    }


    public Long getAmount(){
        return amount;
    }

    public void setAmount(Long amount){
        this.amount = amount;
    }

}
