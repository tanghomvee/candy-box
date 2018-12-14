package com.slst.acct.web.vo;

public class AcctRecordVO {

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 门店Id
     */
    private Long storeId;
    /**
     * 类型A
     */
    private Integer typea;
    /**
     * 类型B
     */
    private Integer typeb;
    /**
     * 开始时间（查询用）
     */
    private String startTime;
    /**
     * 结束时间（查询用）
     */
    private String endTime;
    /**
     * 排序关键字
     */
    private String sortKey;
    /**
     * 排序顺序（升序：ASC，降序：DESC）
     */
    private String orderKey;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getTypea() {
        return typea;
    }

    public void setTypea(Integer typea) {
        this.typea = typea;
    }

    public Integer getTypeb() {
        return typeb;
    }

    public void setTypeb(Integer typeb) {
        this.typeb = typeb;
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

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }
}
