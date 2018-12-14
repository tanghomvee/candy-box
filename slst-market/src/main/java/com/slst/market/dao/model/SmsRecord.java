package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sms_record")
public class SmsRecord extends BaseEntity {
	/**手机号*/
	private String mobile;
	/**用户手机物理地址*/
	private String mac;
	/**用户ID*/
	private Long customerId;
	/**第三方电话暗号ID*/
	private String thirdPartyId;
	/**发送状态1发送成功0正在发送-1失败*/
	private Integer state;
	/**失败原因*/
	private String errmsg;
	/**
	 * 对应的smsBox的ID
	 */
	private Long smsBoxId;

	public Long getSmsBoxId() {
		return smsBoxId;
	}

	public void setSmsBoxId(Long smsBoxId) {
		this.smsBoxId = smsBoxId;
	}

	public String getErrmsg(){
		return errmsg;
	}

	public void setErrmsg(String errmsg){
		this.errmsg = errmsg;
	}


	public Integer getState(){
		return state;
	}

	public void setState(Integer state){
		this.state = state;
	}


	public String getThirdPartyId(){
		return thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId){
		this.thirdPartyId = thirdPartyId;
	}


	public Long getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Long customerId){
		this.customerId = customerId;
	}


	public String getMac(){
		return mac;
	}

	public void setMac(String mac){
		this.mac = mac;
	}


	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

}