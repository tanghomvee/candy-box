package com.candybox.user.dao.model;

import com.candybox.common.dao.model.BaseEntity;

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
    private Date regTime;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 推荐人
     */
    private Long referrer;

    /**
     * 用户微信的OpenID
     */
    private String openId;

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

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getReferrer() {
        return referrer;
    }

    public void setReferrer(Long referrer) {
        this.referrer = referrer;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", regTime=" + regTime +
                ", mobile='" + mobile + '\'' +
                ", referrer=" + referrer +
                ", openId='" + openId + '\'' +
                ", id=" + id +
                ", yn=" + yn +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", changer='" + changer + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }
}