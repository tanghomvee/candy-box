package com.candybox.user.web.vo;

import com.candybox.common.web.vo.BaseVO;

public class UserCandyVO extends BaseVO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 糖果名称
     */
    private String candyName;

    /**
     * 糖果数量
     */
    private Long amt;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCandyName() {
        return candyName;
    }

    public void setCandyName(String candyName) {
        this.candyName = candyName;
    }

    public Long getAmt() {
        return amt;
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "UserCandyVO{" +
                "userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", candyName='" + candyName + '\'' +
                ", amt=" + amt +
                ", id=" + id +
                ", yn=" + yn +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", changer='" + changer + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }
}
