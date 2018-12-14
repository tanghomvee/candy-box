package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_number_pool")
public class NumberPool extends BaseEntity {

    /**
     * 中继号码ID
     */
    private String plId;
    /**
     * 声牙提供ID，暂不知道是何物
     */
    private String uid;
    /**
     * 中继号码
     */
    private String phone;
    /**
     * 区号
     */
    private String areaCode;
    /**
     * 声牙返回字段，暂不知道是何物
     */
    private String appid;
    /**
     * 我们在声牙那边的用户名
     */
    private String myName;
    /**
     * 城市
     */
    private String city;
    /**
     * 省份
     */
    private String province;
    /**
     * 外呼号码唯一ID
     */
    private String iSeatNo;
    /**
     * 声牙返回结果集
     */
    private String result;
    /**
     * 代理商ID
     */
    private Long agentId;
    /**
     * 代理商员工ID
     */
    private Long agentEmpId;
    /**
     * 商家ID
     */
    private Long venderId;
    /**
     * 门店ID
     */
    private Long storeId;

    public String getPlId() {
        return plId;
    }

    public void setPlId(String plId) {
        this.plId = plId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getiSeatNo() {
        return iSeatNo;
    }

    public void setiSeatNo(String iSeatNo) {
        this.iSeatNo = iSeatNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getAgentEmpId() {
        return agentEmpId;
    }

    public void setAgentEmpId(Long agentEmpId) {
        this.agentEmpId = agentEmpId;
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
}
