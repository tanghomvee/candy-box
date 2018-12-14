package com.slst.user.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_user")
public class User extends BaseEntity {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 注册时间
     */
    private Date signDate;
    /**
     * 登录时间
     */
    private Date loginDate;
    /**
     * 注销时间
     */
    private Date closeAcctDate;

    /**
     * 用户类型1系统用户2代理商21.代理商员工3商家31商家员工4数据使用方
     */
    private Integer userType;
    /**
     * 联系电话
     */
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getCloseAcctDate() {
        return closeAcctDate;
    }

    public void setCloseAcctDate(Date closeAcctDate) {
        this.closeAcctDate = closeAcctDate;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}