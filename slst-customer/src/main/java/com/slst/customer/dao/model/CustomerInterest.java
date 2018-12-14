package com.slst.customer.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_interest")
public class CustomerInterest extends BaseEntity {
	/**客户手机物理地址*/
	private String mac;
	/**客户手机号*/
	private String mobile;
	/**客户ID*/
	private Long customerId;
	/**客户兴趣爱好*/
	private String interest;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}