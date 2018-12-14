package com.slst.acct.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_account")
public class Account extends BaseEntity {

    /**
     * 账户名字
     */
    private String acctName;
    /**
     * 代理商划入余额
     */
    private Long leftAgentAmt;
    /**
     * 线上充值余额
     */
    private Long leftRechageAmt;
    /**
     * 累计余额
     */
    private long balance;
    /**
     * 代理商累计划入金额
     */
    private Long totalAgtAmt;
    /**
     * 线上累计充值金额
     */
    private Long totalRchAmt;
    /**
     * 累计充值金额:代理商划入+线上充值
     */
    private Long totalAmt;
    /**
     * 线上充值累计消费
     */
    private Long totalRchCost;
    /**
     * 代理商划入累计消费
     */
    private Long totalAgtCost;
    /**
     * 累计消费
     */
    private Long totalCost;
    /**
     * 冻结金额
     */
    private Long frozen;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 门店ID
     */
    private Long storeId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getFrozen() {
        return frozen;
    }

    public void setFrozen(Long frozen) {
        this.frozen = frozen;
    }


    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Long totalCost) {
        this.totalCost = totalCost;
    }


    public Long getTotalAgtCost() {
        return totalAgtCost;
    }

    public void setTotalAgtCost(Long totalAgtCost) {
        this.totalAgtCost = totalAgtCost;
    }


    public Long getTotalRchCost() {
        return totalRchCost;
    }

    public void setTotalRchCost(Long totalRchCost) {
        this.totalRchCost = totalRchCost;
    }


    public Long getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Long totalAmt) {
        this.totalAmt = totalAmt;
    }


    public Long getTotalRchAmt() {
        return totalRchAmt;
    }

    public void setTotalRchAmt(Long totalRchAmt) {
        this.totalRchAmt = totalRchAmt;
    }


    public Long getTotalAgtAmt() {
        return totalAgtAmt;
    }

    public void setTotalAgtAmt(Long totalAgtAmt) {
        this.totalAgtAmt = totalAgtAmt;
    }


    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }


    public Long getLeftRechageAmt() {
        return leftRechageAmt;
    }

    public void setLeftRechageAmt(Long leftRechageAmt) {
        this.leftRechageAmt = leftRechageAmt;
    }


    public Long getLeftAgentAmt() {
        return leftAgentAmt;
    }

    public void setLeftAgentAmt(Long leftAgentAmt) {
        this.leftAgentAmt = leftAgentAmt;
    }


    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

}
