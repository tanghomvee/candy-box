package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_activity_vender_emp_relation")
public class ActivityVenderEmpRelation extends BaseEntity {

    /** 商家员工ID **/
    private Long venderEmpId;
    /** 活动ID **/
    private Long activityId;

    public Long getVenderEmpId() {
        return venderEmpId;
    }

    public void setVenderEmpId(Long venderEmpId) {
        this.venderEmpId = venderEmpId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
