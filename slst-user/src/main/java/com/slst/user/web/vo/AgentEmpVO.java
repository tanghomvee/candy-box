package com.slst.user.web.vo;

import java.io.Serializable;

public class AgentEmpVO implements Serializable {

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
     * 代理商员工名字
     */
    private String empName;
    /**
     * 所属代理商ID
     */
    private Long agentId;
    /**
     * 代理商员工手机号码
     */
    private String mobile;
    /**
     * 代理商员工地址
     */
    private String address;
    /**
     *代理商员工职位描述
     */
    private String jobDesc;
    /**
     * 是否是业务员
     */
    private Integer isSalesman;
    /**
     * 代理商员工ID
     */
    private Long AgentEmpId;

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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
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

    public Integer getIsSalesman() {
        return isSalesman;
    }

    public void setIsSalesman(Integer isSalesman) {
        this.isSalesman = isSalesman;
    }

    public Long getAgentEmpId() {
        return AgentEmpId;
    }

    public void setAgentEmpId(Long agentEmpId) {
        AgentEmpId = agentEmpId;
    }
}
