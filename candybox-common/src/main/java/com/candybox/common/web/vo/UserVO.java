package com.candybox.common.web.vo;


public class UserVO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 显示名字
     */
    private String displayName;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户类型描述
     */
    private String userTypeDesc;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 代理商ID
     */
    private Long agentId;

    /**
     * 商家ID
     */
    private Long venderId;

    /**
     *
     */
    private String venderName;
    /**
     * 数据使用方Id
     */
    private Long dataUserId;

    /**
     * 代理商员工共ID
     */
    private Long agentEmpId;

    /**
     * 商家员工ID
     */
    private Long venderEmpId;

    /**
     * 门店ID
     */
    private Long storeId;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * 门店名字
     */
    private String storeName;

    /**
     * 账户ID
     */
    private Long acctId;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getDataUserId() {
        return dataUserId;
    }

    public void setDataUserId(Long dataUserId) {
        this.dataUserId = dataUserId;
    }

    public Long getAgentEmpId() {
        return agentEmpId;
    }

    public void setAgentEmpId(Long agentEmpId) {
        this.agentEmpId = agentEmpId;
    }

    public Long getVenderEmpId() {
        return venderEmpId;
    }

    public void setVenderEmpId(Long venderEmpId) {
        this.venderEmpId = venderEmpId;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }


    public String getUserTypeDesc() {
        return userTypeDesc;
    }

    public void setUserTypeDesc(String userTypeDesc) {
        this.userTypeDesc = userTypeDesc;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }
}
