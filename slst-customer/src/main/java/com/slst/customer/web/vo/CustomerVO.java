package com.slst.customer.web.vo;

import com.slst.common.enums.StayStateEnum;
import com.slst.common.web.vo.BaseVO;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-06-06 17:38
 */
public class CustomerVO extends BaseVO {
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 年龄段
     */
    private String ageSlot;

    /**
     * 性別
     */
    private String gender;

    /**
     * 手机MAC
     */
    private String mac;
    /**
     * 首次到店时间
     */
    private Date firstArriveTime;
    /**
     * 到店时间
     */
    private Date arriveTime;
    /**
     * 最近一次到店时间
     */
    private Date recencyArriveTime;

    /**
     * 离店时间
     */
    private Date leaveTime;

    /**
     * 到店次数
     */
    private Integer nums;

    /**
     * 平均驻留时间：分钟
     */
    private Double avgStayTime;
    /**
     * 驻留时间：分钟
     */
    private Double stayTime;
    /**
     * 手机厂商
     */
    private String mobileBrand;

    /**
     * @see StayStateEnum
     * 客户停留状态
     */
    private String stayState;
    private String apps;
    private String interests;
    /**
     * @see com.slst.common.enums.GenderEnum
     */
    private Integer sex;
    /**年龄*/
    private Integer age;
    /**客户信息所对应的客户设备ID*/
    private Long deviceId;

    /**客户所有标签*/
    private String tags;

    /**客户小孩情况*/
    private String children;
    /**客户汽车情况*/
    private String car;
    /**客户教育情况*/
    private String education;
    /**客户职业情况*/
    private String career;
    /**客户收入情况*/
    private String incomeSlot;
    /**常驻城市*/
    private String permCity;

    /**设备档次*/
    private String mdLevel;

    /**房产情况*/
    private String house;
    /**婚姻情况*/
    private String married;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAgeSlot() {
        return ageSlot;
    }

    public void setAgeSlot(String ageSlot) {
        this.ageSlot = ageSlot;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Date getFirstArriveTime() {
        return firstArriveTime;
    }

    public void setFirstArriveTime(Date firstArriveTime) {
        this.firstArriveTime = firstArriveTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getRecencyArriveTime() {
        return recencyArriveTime;
    }

    public void setRecencyArriveTime(Date recencyArriveTime) {
        this.recencyArriveTime = recencyArriveTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Double getAvgStayTime() {
        return avgStayTime;
    }

    public void setAvgStayTime(Double avgStayTime) {
        this.avgStayTime = avgStayTime;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public Double getStayTime() {
        return stayTime;
    }

    public void setStayTime(Double stayTime) {
        this.stayTime = stayTime;
    }

    public String getStayState() {
        return stayState;
    }

    public void setStayState(String stayState) {
        this.stayState = stayState;
    }

    public void setApps(String apps) {
        this.apps = apps;
    }

    public String getApps() {
        return apps;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getInterests() {
        return interests;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getIncomeSlot() {
        return incomeSlot;
    }

    public void setIncomeSlot(String incomeSlot) {
        this.incomeSlot = incomeSlot;
    }

    public String getPermCity() {
        return permCity;
    }

    public void setPermCity(String permCity) {
        this.permCity = permCity;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMdLevel() {
        return mdLevel;
    }

    public void setMdLevel(String mdLevel) {
        this.mdLevel = mdLevel;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }
}
