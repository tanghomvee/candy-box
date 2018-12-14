package com.slst.market.web.vo;

/**
 * @author: daiyou.zhong
 * @description: 短信发送请求VO
 * @create: 2018-09-07 09:55
 * @version: 2.0
 **/
public class SmsSendReqVO {

    /** 短信模板ID **/
    private Long smsTplId;
    /** 活动ID **/
    private Long activityId;
    /** 客户数量 **/
    private Integer customerCount;

    public Long getSmsTplId() {
        return smsTplId;
    }

    public void setSmsTplId(Long smsTplId) {
        this.smsTplId = smsTplId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    @Override
    public String toString() {
        return "SmsSendReqVO{" +
                "smsTplId=" + smsTplId +
                ", activityId=" + activityId +
                ", customerCount=" + customerCount +
                '}';
    }
}
