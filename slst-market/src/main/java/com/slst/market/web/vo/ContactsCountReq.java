package com.slst.market.web.vo;

import java.io.Serializable;

/**
 * @author: daiyou.zhong
 * @description: 商家员工分多少条客户数据
 * @create: 2018-09-17 13:54
 * @version: 2.0
 **/
public class ContactsCountReq implements Serializable {
    private static final long serialVersionUID = -7209181546196637806L;
    /** 员工Id **/
    private Long venderEmpId;
    /** 商家给员工分的客户数量 **/
    private Integer contactCount;

    public Long getVenderEmpId() {
        return venderEmpId;
    }

    public void setVenderEmpId(Long venderEmpId) {
        this.venderEmpId = venderEmpId;
    }

    public Integer getContactCount() {
        return contactCount;
    }

    public void setContactCount(Integer contactCount) {
        this.contactCount = contactCount;
    }
}
