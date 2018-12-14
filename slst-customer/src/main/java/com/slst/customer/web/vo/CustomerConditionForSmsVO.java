package com.slst.customer.web.vo;

import java.util.List;

public class CustomerConditionForSmsVO {

    /**
     *性别
     */
    private Integer sex;
    /**
     *年龄段
     */
    private List<String> ageSlot;
    /**
     *手机品牌List
     */
    private List<String> mobileBrand;
    /**
     *收入情况
     */
    private List<String> incomeSlot;
    /**
     *职业
     */
    private List<String> career;
    /**
     *车产情况
     */
    private String car;
    /**
     *教育程度
     */
    private List<String> education;
    /**
     *小孩情况
     */
    private List<String> children;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 查询时间的类型0本日,1本周,2本月,3自定义时段
     */
    private Integer searchType;

    /**
     *商家ID
     */
    private Long venderId;
    /**
     *筛选到店的开始日期
     */
    private String startTime;
    /**
     *筛选到店的结束日期
     */
    private String endTime;

    /**
     *筛选到店的开始时间
     */
    private String startHour;
    /**
     *筛选到店的结束时间
     */
    private String endHour;

    /**
     *筛选驻留时间的最小值
     */
    private Integer minStayTime;
    /**
     *筛选驻留时间的最大值
     */
    private Integer maxStayTime;
    /**
     *最小到店次数
     */
    private Integer minComeTimes;
    /**
     *最大到店次数
     */
    private Integer maxComeTimes;


    /**
     *已发送过短信的最小时间
     */
    private String minSendTime;
    /**
     *已发送过短信的最大时间
     */
    private String maxSendTime;

    /**
     * 有房or无房
     */
    private String house;

    /**
     *兴趣爱好
     */
    private List<String> interest;

    /**
     * 活动ID
     */
    private Long activityId;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public List<String> getAgeSlot() {
        return ageSlot;
    }

    public void setAgeSlot(List<String> ageSlot) {
        this.ageSlot = ageSlot;
    }

    public List<String> getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(List<String> mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public List<String> getIncomeSlot() {
        return incomeSlot;
    }

    public void setIncomeSlot(List<String> incomeSlot) {
        this.incomeSlot = incomeSlot;
    }

    public List<String> getCareer() {
        return career;
    }

    public void setCareer(List<String> career) {
        this.career = career;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getMinStayTime() {
        return minStayTime;
    }

    public void setMinStayTime(Integer minStayTime) {
        this.minStayTime = minStayTime;
    }

    public Integer getMaxStayTime() {
        return maxStayTime;
    }

    public void setMaxStayTime(Integer maxStayTime) {
        this.maxStayTime = maxStayTime;
    }

    public Integer getMinComeTimes() {
        return minComeTimes;
    }

    public void setMinComeTimes(Integer minComeTimes) {
        this.minComeTimes = minComeTimes;
    }

    public Integer getMaxComeTimes() {
        return maxComeTimes;
    }

    public void setMaxComeTimes(Integer maxComeTimes) {
        this.maxComeTimes = maxComeTimes;
    }

    public String getMinSendTime() {
        return minSendTime;
    }

    public void setMinSendTime(String minSendTime) {
        this.minSendTime = minSendTime;
    }

    public String getMaxSendTime() {
        return maxSendTime;
    }

    public void setMaxSendTime(String maxSendTime) {
        this.maxSendTime = maxSendTime;
    }

    public List<String> getInterest() {
        return interest;
    }

    public void setInterest(List<String> interest) {
        this.interest = interest;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
