package com.candybox.user.web.vo;

import com.candybox.common.web.vo.BaseVO;

import java.util.Date;

public class UserVO extends BaseVO {
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
     * 推荐人用户名
     */
    private Long referrerName;

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

    public Long getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(Long referrerName) {
        this.referrerName = referrerName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", regTime=" + regTime +
                ", mobile='" + mobile + '\'' +
                ", referrerName=" + referrerName +
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
