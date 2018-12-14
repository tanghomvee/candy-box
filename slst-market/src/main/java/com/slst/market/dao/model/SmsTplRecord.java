package com.slst.market.dao.model;

import com.slst.common.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "t_sms_tpl_record")
public class SmsTplRecord extends BaseEntity {
	/**数据使用方ID*/
	private Date dataUserId;
	/**短信模板标题*/
	private String title;
	/**短信内容*/
	private String content;
	/**模板签名*/
	private String sign;
	/**第三方暗号ID*/
	private String thirdPartyId;
	/**创建模板时间*/
	private Date time;
	/**模板审核状态1审核通过0正在审核-1审核失败*/
	private Integer states;
	/**审核失败原因*/
	private String errMsg;

	public String getErrMsg(){
		return errMsg;
	}

	public void setErrMsg(String errMsg){
		this.errMsg = errMsg;
	}


	public Integer getStates(){
		return states;
	}

	public void setStates(Integer states){
		this.states = states;
	}


	public Date getTime(){
		return time;
	}

	public void setTime(Date time){
		this.time = time;
	}


	public String getThirdPartyId(){
		return thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId){
		this.thirdPartyId = thirdPartyId;
	}


	public String getSign(){
		return sign;
	}

	public void setSign(String sign){
		this.sign = sign;
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


	public Date getDataUserId(){
		return dataUserId;
	}

	public void setDataUserId(Date dataUserId){
		this.dataUserId = dataUserId;
	}

}