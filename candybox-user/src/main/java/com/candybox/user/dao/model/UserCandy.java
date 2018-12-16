package com.candybox.user.dao.model;

import com.candybox.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_user_candy")
public class UserCandy extends BaseEntity {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 糖果ID
     */
    private Long candyId;

    /**
     * 糖果数量
     */
    private Long amt;


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

    @Override
    public String toString() {
        return "UserCandy{" +
                "userId=" + userId +
                ", candyId=" + candyId +
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