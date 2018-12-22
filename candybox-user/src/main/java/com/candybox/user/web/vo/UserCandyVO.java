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
     * 提供糖果的商家名称
     */
    private String venderName;


    /**
     * 糖果图标路径
     */
    private String icon;

    /**
     * 糖果描述信息
     */
    private String detail;

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

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "UserCandyVO{" +
                "userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", candyName='" + candyName + '\'' +
                ", venderName='" + venderName + '\'' +
                ", icon='" + icon + '\'' +
                ", detail='" + detail + '\'' +
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
