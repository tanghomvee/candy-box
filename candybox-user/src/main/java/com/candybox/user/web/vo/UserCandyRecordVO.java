package com.candybox.user.web.vo;

import com.candybox.common.web.vo.BaseVO;

import java.util.Date;

public class UserCandyRecordVO extends BaseVO {
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
     * 操作的糖果数量
     */
    private Long amt;

    /**
     * 操作前用户糖果的数量
     */
    private Long beforAmt;

    /**
     * 操作后用户糖果的数量
     */
    private Long afterAmt;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * @see com.candybox.common.enums.OperateKindEnum
     * 操作类型
     */
    private Integer kind;

    /**
     * 操作备注
     */
    private String remark;


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

    public Long getBeforAmt() {
        return beforAmt;
    }

    public void setBeforAmt(Long beforAmt) {
        this.beforAmt = beforAmt;
    }

    public Long getAfterAmt() {
        return afterAmt;
    }

    public void setAfterAmt(Long afterAmt) {
        this.afterAmt = afterAmt;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public String toString() {
        return "UserCandyRecordVO{" +
                "userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", candyName='" + candyName + '\'' +
                ", amt=" + amt +
                ", beforAmt=" + beforAmt +
                ", afterAmt=" + afterAmt +
                ", operateTime=" + operateTime +
                ", kind=" + kind +
                ", remark='" + remark + '\'' +
                ", id=" + id +
                ", yn=" + yn +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", changer='" + changer + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }
}
