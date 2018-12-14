package com.slst.user.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_match_record_temp")
public class MatchRecordTemp extends BaseEntity {
    /**匹配类型:1:电话匹配,2:标签匹配*/
    private Integer type;
    /**匹配结果*/
    private String rsp;
    /**用户ID*/
    private Long userId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRsp() {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}