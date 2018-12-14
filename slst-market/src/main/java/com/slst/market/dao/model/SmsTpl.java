package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sms_tpl")
public class SmsTpl extends BaseEntity {
	/**模板标题*/
	private String title;
	/**短信签名*/
	private String signname;
	/**短信内容*/
	private String content;
	/**第三方短信模板ID*/
	private String thirdPartyId;
	/**审核状态:1通过0审核中-1审核失败*/
	private Integer state;
	/**失败原因*/
	private String errmsg;
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


	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}


	public String getSignname(){
		return signname;
	}

	public void setSignname(String signname){
		this.signname = signname;
	}


	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

}