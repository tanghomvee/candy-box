package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "t_sms_box")
public class SmsBox extends BaseEntity {
	/**模板标题*/
	private String title;
	/**短信内容*/
	private String content;
	/**第三方短信模板ID*/
	private String thirdPartyId;
	/**短信模板ID*/
	private Long smsTmpId;
	/**发送时间*/
	private Date sendTime;
	/**短信单价*/
	private Long price;
	/**发送短信条数*/
	private Long count;
	/**活动ID*/
	private Long activityId;
	/**账户交易记录ID用于查看冻结金额*/
	private Long acctRecordId;
	/**用户ID*/
	private Long userId;
	/**商家ID*/
	private Long venderId;
	/**商家名字*/
	private String venderName;
	/**门店ID*/
	private Long storeId;
	/**门店名字*/
	private String storeName;

	public String getStoreName(){
		return storeName;
	}

	public void setStoreName(String storeName){
		this.storeName = storeName;
	}


	public Long getStoreId(){
		return storeId;
	}

	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}


	public String getVenderName(){
		return venderName;
	}

	public void setVenderName(String venderName){
		this.venderName = venderName;
	}


	public Long getVenderId(){
		return venderId;
	}

	public void setVenderId(Long venderId){
		this.venderId = venderId;
	}


	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}


	public Long getAcctRecordId(){
		return acctRecordId;
	}

	public void setAcctRecordId(Long acctRecordId){
		this.acctRecordId = acctRecordId;
	}


	public Long getActivityId(){
		return activityId;
	}

	public void setActivityId(Long activityId){
		this.activityId = activityId;
	}


	public Long getCount(){
		return count;
	}

	public void setCount(Long count){
		this.count = count;
	}


	public Long getPrice(){
		return price;
	}

	public void setPrice(Long price){
		this.price = price;
	}


	public Date getSendTime(){
		return sendTime;
	}

	public void setSendTime(Date sendTime){
		this.sendTime = sendTime;
	}


	public Long getSmsTmpId(){
		return smsTmpId;
	}

	public void setSmsTmpId(Long smsTmpId){
		this.smsTmpId = smsTmpId;
	}


	public String getThirdPartyId(){
		return thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId){
		this.thirdPartyId = thirdPartyId;
	}


	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}


	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

}