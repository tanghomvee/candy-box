package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "t_sms_send_record")
public class SmsSendRecord extends BaseEntity {
	/**数据使用方ID*/
	private Long dataUserId;
	/**短信标题*/
	private String title;
	/**短信内容*/
	private String smsContent;
	/**接收方手机号码*/
	private String mobile;
	/**第三方手机暗号ID*/
	private String thirdPartyId;
	/**发送状态1.成功0发送中-1发送失败*/
	private Integer state;
	/**发送失败原因*/
	private String errMsg;
	/**发送时间*/
	private Date time;
	/**该次发送费用*/
	private Long price;

	public Long getPrice(){
		return price;
	}

	public void setPrice(Long price){
		this.price = price;
	}


	public Date getTime(){
		return time;
	}

	public void setTime(Date time){
		this.time = time;
	}


	public String getErrMsg(){
		return errMsg;
	}

	public void setErrMsg(String errMsg){
		this.errMsg = errMsg;
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


	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}


	public String getSmsContent(){
		return smsContent;
	}

	public void setSmsContent(String smsContent){
		this.smsContent = smsContent;
	}


	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}


	public Long getDataUserId(){
		return dataUserId;
	}

	public void setDataUserId(Long dataUserId){
		this.dataUserId = dataUserId;
	}

}