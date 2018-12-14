package com.slst.agent.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_agent_emp")
public class AgentEmp extends BaseEntity {

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
     * 代理商员工的用户ID
     */
    private Long userId;


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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
