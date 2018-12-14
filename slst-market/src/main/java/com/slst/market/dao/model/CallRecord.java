package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_call_record")
public class CallRecord extends BaseEntity {

    private String fromNum;
    private String toNum;
    private String toNumId;
    private String relayNum;
    private String relayNumId;
    private Long duration;
    private Long fee;
    private String callId;
    private String billId;
    private Date calltime;
    private Long userId;
    private Long venderId;
    private Long storeId;
    private Long acctRcdId;
    private Long contactsId;

    //TODO 待删除
    private Long customerId;

    private Long activityId;

    private Integer intention;

    private String remark;


    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }

    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }

    public String getToNumId() {
        return toNumId;
    }

    public void setToNumId(String toNumId) {
        this.toNumId = toNumId;
    }

    public String getRelayNum() {
        return relayNum;
    }

    public void setRelayNum(String relayNum) {
        this.relayNum = relayNum;
    }

    public String getRelayNumId() {
        return relayNumId;
    }

    public void setRelayNumId(String relayNumId) {
        this.relayNumId = relayNumId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Date getCalltime() {
        return calltime;
    }

    public void setCalltime(Date calltime) {
        this.calltime = calltime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getAcctRcdId() {
        return acctRcdId;
    }

    public void setAcctRcdId(Long acctRcdId) {
        this.acctRcdId = acctRcdId;
    }

    public Long getContactsId() {
        return contactsId;
    }

    public void setContactsId(Long contactsId) {
        this.contactsId = contactsId;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        customerId=0L;
        this.customerId = customerId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        activityId=0L;
        this.activityId = activityId;
    }

    public Integer getIntention() {
        return intention;
    }

    public void setIntention(Integer intention) {
        intention=-1;
        this.intention = intention;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        remark="我今天的测试数据";
        this.remark = remark;
    }
}
