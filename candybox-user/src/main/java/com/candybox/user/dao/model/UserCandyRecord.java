package com.candybox.user.dao.model;

import com.candybox.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_user_candy_record")
public class UserCandyRecord extends BaseEntity {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 糖果ID
     */
    private Long candyId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCandyId() {
        return candyId;
    }

    public void setCandyId(Long candyId) {
        this.candyId = candyId;
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
        return "UserCandyRecord{" +
                "userId=" + userId +
                ", candyId=" + candyId +
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