package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_contacts")
public class Contacts extends BaseEntity {
    /**客户ID*/
    private Long customerId;
    /**主叫号码*/
    private String fromNum;
    /**被叫号码*/
    private String toNum;
    /**被叫号码ID*/
    private String toNumId;
    /**拨打电话的用户ID*/
    private Long userId;
    /**拨打电话的商家ID*/
    private Long venderId;
    /**商家员工Id*/
    private Long venderEmpId;
    /**拨打电话的门店ID*/
    private Long storeId;
    /**活动ID*/
    private Long activityId;
    /**营销次数*/
    private Integer touchtimes;
    /**意向等级*/
    private Integer intention;
    /**备注信息*/
    private String remark;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

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

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getTouchtimes() {
        return touchtimes;
    }

    public void setTouchtimes(Integer touchtimes) {
        this.touchtimes = touchtimes;
    }

    public Integer getIntention() {
        return intention;
    }

    public void setIntention(Integer intention) {
        this.intention = intention;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVenderEmpId() {
        return venderEmpId;
    }

    public void setVenderEmpId(Long venderEmpId) {
        this.venderEmpId = venderEmpId;
    }
}