package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sms_fee")
public class SmsFee extends BaseEntity {
	/**商家ID*/
	private Long venderId;
	/**短信费用*/
	private Long fee;

	public Long getFee(){
		return fee;
	}

	public void setFee(Long fee){
		this.fee = fee;
	}


	public Long getVenderId(){
		return venderId;
	}

	public void setVenderId(Long venderId){
		this.venderId = venderId;
	}

}