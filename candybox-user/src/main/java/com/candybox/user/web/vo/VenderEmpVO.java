package com.candybox.user.web.vo;

import java.io.Serializable;

public class VenderEmpVO implements Serializable {
//empName，storeId，venderId，mobile，address，jobDesc，userId，yn，creator，createTime，changer，changeTime
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 员工名字
     */
    private String empName;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 商家ID
     */
    private Long venderId;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 职位描述
     */
    private String jobDesc;
    /**
     * 商家员工ID
     */
    private Long venderEmpId;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public Long getVenderEmpId() {
        return venderEmpId;
    }

    public void setVenderEmpId(Long venderEmpId) {
        this.venderEmpId = venderEmpId;
    }
}
