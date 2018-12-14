package com.slst.market.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: daiyou.zhong
 * @description: 客户分配请求对象
 * @create: 2018-09-17 11:47
 * @version: 2.0
 **/
public class ContactsSaveByActivityIdReq implements Serializable {
    private static final long serialVersionUID = -1376336210336176884L;
    /** 活动Id **/
    private Long activityId;
    /** 是否分给员工的状态：0-不分; 1-分给员工 **/
    private Integer status;
    /** 商家给自己员工分的客户数量 **/
    private List<ContactsCountReq> contactsCountReqs;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ContactsCountReq> getContactsCountReqs() {
        return contactsCountReqs;
    }

    public void setContactsCountReqs(List<ContactsCountReq> contactsCountReqs) {
        this.contactsCountReqs = contactsCountReqs;
    }
}
